package com.moon.service;

import com.moon.vo.OrderReportVO;
import com.moon.vo.SalesTop10ReportVO;
import com.moon.vo.TurnoverReportVO;
import com.moon.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO turnover(LocalDate begin, LocalDate end);

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    UserReportVO user(LocalDate begin, LocalDate end);

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO order(LocalDate begin, LocalDate end);

    /**
     * 销量统计
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO sale(LocalDate begin, LocalDate end);
}
