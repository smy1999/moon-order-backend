package com.moon.service;

import com.moon.vo.OrderReportVO;
import com.moon.vo.SalesTop10ReportVO;
import com.moon.vo.TurnoverReportVO;
import com.moon.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {

    /**
     * 营业额统计
     */
    TurnoverReportVO turnover(LocalDate begin, LocalDate end);

    /**
     * 用户统计
     */
    UserReportVO user(LocalDate begin, LocalDate end);

    /**
     * 订单统计
     */
    OrderReportVO order(LocalDate begin, LocalDate end);

    /**
     * 销量统计
     */
    SalesTop10ReportVO sale(LocalDate begin, LocalDate end);

    /**
     * 导出Excel报表
     */
    void export(HttpServletResponse resp);

}
