package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/7 12:19
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/7,13:41
     **/
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        StringBuilder sbDate = new StringBuilder();
        StringBuilder sbTurnover = new StringBuilder();
        //sbDate 拼接日期
        while (begin.isBefore(end)) {
            sbDate.append(begin.toString()).append(",");
            //查询当日营业额,如果营业额为null,则返回0.0
            Double amount = reportMapper.amount(begin);
            if (amount == null) {
                amount = 0.0;
            }
            //sbTurnover 拼接营业额
            sbTurnover.append(amount).append(",");
            begin = begin.plusDays(1);
        }
        //拼接最后一天的日期和营业额
        String dateList = sbDate.append(end.toString()).toString();
        Double amount = reportMapper.amount(end);
        if (amount == null) {
            amount = 0.0;
        }
        sbTurnover.append(amount);
        String turnOverList = sbTurnover.toString();

        TurnoverReportVO turnoverReportVO = new TurnoverReportVO(dateList, turnOverList);
        return turnoverReportVO;
    }

    /**
     * 用户统计
     *
     * @param begin
     * @param end
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/7,13:39
     **/
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        StringBuilder sbDate = new StringBuilder();
        StringBuilder sbNew = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        while (begin.isBefore(end)) {
            sbDate.append(begin.toString()).append(",");
            //查询当日新增用户数量
            Integer newUser = reportMapper.newUser(begin);
            if (newUser == null) {
                newUser = 0;
            }
            //拼接当日新增用户数量
            sbNew.append(newUser).append(",");
            //查询戒指当日用户总量
            Integer total = reportMapper.totalUser(begin);
            sbTotal.append(total).append(",");
            begin = begin.plusDays(1);
        }
        //拼接最后一天新增用户数量
        Integer newUser = reportMapper.newUser(end);
        String newUserList = sbNew.append(newUser).toString();
        //拼接最后一天日期
        String dateList = sbDate.append(end.toString()).toString();
        //拼接截至最后一天用户总量
        Integer total = reportMapper.totalUser(begin);
        String totalUserList = sbTotal.append(total).toString();
        UserReportVO userReportVO = new UserReportVO(dateList, totalUserList, newUserList);
        return userReportVO;
    }

    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/7,14:09
     **/
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        StringBuilder sbDate = new StringBuilder();
        StringBuilder sbOrderCount = new StringBuilder();
        StringBuilder sbDoneDaily = new StringBuilder();
        while (begin.isBefore(end)) {
            sbDate.append(begin.toString()).append(",");
            //统计每日订单数量
            Integer orderCount = reportMapper.orderCount(begin);

            sbOrderCount.append(orderCount).append(",");
            //统计每日有效订单数量status=5
            Integer orderDaily = reportMapper.doneOrderDaily(begin);
            sbDoneDaily.append(orderDaily).append(",");
            begin = begin.plusDays(1);
        }
        //拼接最后一天日期
        String dateList = sbDate.append(end.toString()).toString();
        //拼接最后一天订单数量
        Integer orderCount = reportMapper.orderCount(end);
        String orderCountList = sbOrderCount.append(orderCount).toString();
        //拼接最后一天有效订单数量
        Integer orderDaily = reportMapper.doneOrderDaily(end);
        String validOrderCountList = sbDoneDaily.append(orderDaily).toString();
        //统计订单总数量
        Integer totalOrderCount = reportMapper.orderTotal(begin,end);
        //统计有效订单总数量
        Integer validOrderCount = reportMapper.doneOrderTotal(begin,end);
        //统计订单完成率 = 有效订单总数量/订单总数量
        Double orderCompletionRate = (double) validOrderCount / totalOrderCount;
        OrderReportVO orderReportVO = new OrderReportVO();
        orderReportVO.setDateList(dateList);
        orderReportVO.setOrderCountList(orderCountList);
        orderReportVO.setValidOrderCountList(validOrderCountList);
        orderReportVO.setTotalOrderCount(totalOrderCount);
        orderReportVO.setValidOrderCount(validOrderCount);
        orderReportVO.setOrderCompletionRate(orderCompletionRate);
        return orderReportVO;
    }
    /**
     * 商品热销top10
     * @param begin
     * @param end
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/7,15:36
     **/
    @Override
    public Object top10(LocalDate begin, LocalDate end) {
        LocalDateTime begin1 = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime end1 = LocalDateTime.of(end, LocalTime.MAX);
        LocalDateTime localDateTime = end1.plusDays(1);
        StringBuilder sbName= new StringBuilder();
        StringBuilder sbCount= new StringBuilder();
        //统计销量前十的商品
        List<GoodsSalesDTO> goodsSalesDTOS = reportMapper.top10(begin1, localDateTime);
        System.out.println(goodsSalesDTOS);
        for (GoodsSalesDTO salesDTO : goodsSalesDTOS) {
            sbName.append(salesDTO.getName()).append(",");
            sbCount.append(salesDTO.getNumber()).append(",");
        }
        String a = sbName.toString();
        String b = sbCount.toString();
        SalesTop10ReportVO top10=new SalesTop10ReportVO(a,b);
        return top10;
    }
}
