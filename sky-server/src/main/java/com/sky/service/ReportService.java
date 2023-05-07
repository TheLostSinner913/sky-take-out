package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/7 12:19
 */
public interface ReportService {

    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    UserReportVO userStatistics(LocalDate begin, LocalDate end);

    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);

    Object top10(LocalDate begin, LocalDate end);
}
