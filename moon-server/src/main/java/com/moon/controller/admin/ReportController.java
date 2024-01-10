package com.moon.controller.admin;

import com.moon.result.Result;
import com.moon.service.ReportService;
import com.moon.vo.OrderReportVO;
import com.moon.vo.SalesTop10ReportVO;
import com.moon.vo.TurnoverReportVO;
import com.moon.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RequestMapping("/admin/report")
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 销量top10统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> sale(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ) {
        log.info("销量统计 {} - {}", begin, end);
        SalesTop10ReportVO report =  reportService.sale(begin, end);
        return Result.success(report);
    }


    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> order(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ) {
        log.info("订单统计 {} - {}", begin, end);
        OrderReportVO report =  reportService.order(begin, end);
        return Result.success(report);
    }


    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    public Result<UserReportVO> user(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ) {
        log.info("用户统计 {} - {}", begin, end);
        UserReportVO report =  reportService.user(begin, end);
        return Result.success(report);
    }


    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnover(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ) {
        log.info("营业额统计, {} - {}", begin, end);
        TurnoverReportVO report = reportService.turnover(begin, end);
        return Result.success(report);
    }

}
