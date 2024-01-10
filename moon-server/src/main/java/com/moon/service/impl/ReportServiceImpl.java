package com.moon.service.impl;

import com.moon.dto.GoodsSalesDTO;
import com.moon.entity.Orders;
import com.moon.mapper.ReportMapper;
import com.moon.service.ReportService;
import com.moon.vo.OrderReportVO;
import com.moon.vo.SalesTop10ReportVO;
import com.moon.vo.TurnoverReportVO;
import com.moon.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


@Service
public class ReportServiceImpl implements ReportService {


    @Autowired
    private ReportMapper reportMapper;

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

        return OrderReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderList, ","))
                .validOrderCountList(StringUtils.join(validOrderList, ","))
                .validOrderCount(validOrderCount)
                .totalOrderCount(totalOrderCount)
                .orderCompletionRate(validOrderCount / (0.0 + totalOrderCount))
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

}
