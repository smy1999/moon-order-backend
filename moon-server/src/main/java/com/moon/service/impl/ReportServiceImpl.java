package com.moon.service.impl;

import com.moon.dto.GoodsSalesDTO;
import com.moon.entity.Orders;
import com.moon.mapper.ReportMapper;
import com.moon.service.ReportService;
import com.moon.service.WorkspaceService;
import com.moon.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class ReportServiceImpl implements ReportService {


    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private WorkspaceService workspaceService;

    @Override
    public TurnoverReportVO turnover(LocalDate begin, LocalDate end) {

        List<LocalDate> dateList = new ArrayList<>();
        LocalDate iter = begin;
        while (iter.isBefore(end.plusDays(1))) {
            dateList.add(iter);
            iter = iter.plusDays(1);
        }

        List<Double> turnovers = dateList.stream().map(date -> {
            Map<String, Object> param = new HashMap<>();
            param.put("begin", LocalDateTime.of(date, LocalTime.MIN));
            param.put("end", LocalDateTime.of(date, LocalTime.MAX));
            param.put("status", Orders.COMPLETED);

            Double sum = reportMapper.sumTurnoverByMap(param);
            sum = sum == null ? 0.0 : sum;
            return sum;
        }).toList();

        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnovers, ","))
                .build();
    }

    @Override
    public UserReportVO user(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate iter = begin;
        while (iter.isBefore(end.plusDays(1))) {
            dateList.add(iter);
            iter = iter.plusDays(1);
        }

        List<Integer> userList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();

        dateList.forEach(date -> {
            Map<String, Object> param = new HashMap<>();
            param.put("end", LocalDateTime.of(date, LocalTime.MAX));
            Integer user = reportMapper.sumUserByMap(param);
            userList.add(user);

            param.put("begin", LocalDateTime.of(date, LocalTime.MIN));
            Integer newUser = reportMapper.sumUserByMap(param);
            newUserList.add(newUser);
        });

        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(userList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    @Override
    public OrderReportVO order(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate iter = begin;
        while (iter.isBefore(end.plusDays(1))) {
            dateList.add(iter);
            iter = iter.plusDays(1);
        }

        List<Integer> orderList = new ArrayList<>();
        List<Integer> validOrderList = new ArrayList<>();
        Integer totalOrderCount = 0;
        Integer validOrderCount = 0;

        for (LocalDate date : dateList) {
            Map<String, Object> param = new HashMap<>();

            param.put("end", LocalDateTime.of(date, LocalTime.MAX));
            param.put("begin", LocalDateTime.of(date, LocalTime.MIN));
            Integer order = reportMapper.sumOrderByMap(param);
            orderList.add(order);
            totalOrderCount += order;

            param.put("status", Orders.COMPLETED);
            Integer validOrder = reportMapper.sumOrderByMap(param);
            validOrderList.add(validOrder);
            validOrderCount += validOrder;
        }

        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }

        return OrderReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderList, ","))
                .validOrderCountList(StringUtils.join(validOrderList, ","))
                .validOrderCount(validOrderCount)
                .totalOrderCount(totalOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO sale(LocalDate begin, LocalDate end) {

        Map<String, Object> param = new HashMap<>();

        param.put("begin", LocalDateTime.of(begin, LocalTime.MIN));
        param.put("end", LocalDateTime.of(end, LocalTime.MAX));

        List<GoodsSalesDTO> sales = reportMapper.findTopSales(param);

        List<String> names = sales.stream().map(GoodsSalesDTO::getName).toList();
        List<Integer> numbers = sales.stream().map(GoodsSalesDTO::getNumber).toList();

        return SalesTop10ReportVO
                .builder()
                .numberList(StringUtils.join(numbers, ","))
                .nameList(StringUtils.join(names, ","))
                .build();
    }

    @Override
    public void export(HttpServletResponse resp) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {

            XSSFWorkbook excel = new XSSFWorkbook(in);
            XSSFSheet sheet = excel.getSheetAt(0);

            // 处理总数据
            LocalDate end = LocalDate.now().minusDays(1);
            LocalDate begin = LocalDate.now().minusDays(30);

            String dateInfo = begin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " to " + end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            sheet.getRow(2).getCell(2).setCellValue(dateInfo);

            BusinessDataVO total = workspaceService.business(begin, end);
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(total.getTurnover());
            row.getCell(4).setCellValue(total.getOrderCompletionRate());
            row.getCell(6).setCellValue(total.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(total.getValidOrderCount());
            row.getCell(4).setCellValue(total.getUnitPrice());

            // 处理每天数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                BusinessDataVO business = workspaceService.business(date, date);

                row = sheet.getRow(i + 7);
                row.getCell(1).setCellValue(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                row.getCell(2).setCellValue(business.getTurnover());
                row.getCell(3).setCellValue(business.getValidOrderCount());
                row.getCell(4).setCellValue(business.getOrderCompletionRate());
                row.getCell(5).setCellValue(business.getUnitPrice());
                row.getCell(6).setCellValue(business.getNewUsers());
            }

            ServletOutputStream out = resp.getOutputStream();
            excel.write(out);

            out.close();
            excel.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
