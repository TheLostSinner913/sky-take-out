package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/7 12:17
 */
@RestController
@Slf4j
@Api(tags = "报表管理")
@RequestMapping("/admin/report")
public class reportController {
    @Autowired
    private ReportService reportService;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/7,13:41
     **/
    @GetMapping("/turnoverStatistics")
    @ApiOperation(value = "营业额统计")
    public Result turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin
            , @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("营业额统计{}-{}", begin, end);
        TurnoverReportVO turnoverReportVO = reportService.turnoverStatistics(begin, end);
        return Result.success(turnoverReportVO);
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
    @GetMapping("/userStatistics")
    @ApiOperation(value = "用户统计")
    public Result userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin
            , @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("用户统计{}-{}", begin, end);
        UserReportVO userReportVO = reportService.userStatistics(begin, end);
        return Result.success(userReportVO);
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
    @GetMapping("/ordersStatistics")
    @ApiOperation(value = "订单统计")
    public Result ordersStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin
            , @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("订单统计{}-{}", begin, end);
        OrderReportVO orderReportVO = reportService.ordersStatistics(begin, end);
        return Result.success(orderReportVO);
    }

    /**
     * 商品热销top10
     * @param begin
     * @param end
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/7,15:36
     **/
    @GetMapping("/top10")
    @ApiOperation(value = "商品销量top10")
    public Result top10(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin
            , @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("商品销量top10{}-{}", begin, end);
        return Result.success(reportService.top10(begin, end));
    }
    @GetMapping("/export")
    @ApiOperation(value = "导出报表")
    public void export(HttpServletResponse response) throws IOException {
        reportService.export(response);
    }
}
