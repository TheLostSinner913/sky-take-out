package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/3 21:43
 */
@RestController
@Slf4j
@Api(tags = "管理员订单管理")
@RequestMapping("/admin/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单分页查询
     *
     * @param beginTime
     * @param endTime
     * @param number
     * @param page
     * @param pageSize
     * @param phone
     * @param status
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/3,23:58
     **/
    @GetMapping("/conditionSearch")
    @ApiOperation(value = "条件查询", notes = "条件查询")
    public Result conditionSearch(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                  Integer number, Integer page,
                                  Integer pageSize, String phone,
                                  Integer status) {
        log.info("管理员订单管理-条件查询{},{},{},{},{},{},{}", beginTime, endTime, number, page, pageSize, phone, status);
        Result result = orderService.conditionSearch(page, pageSize, beginTime, endTime, number, phone, status);
        return result;
    }

    /**
     * @param cancelDTO
     * @return com.sky.result.Result
     * @Description: 取消订单
     * @author 刘东钦
     * @create 2023/5/3,23:07
     **/
    @PutMapping("/cancel")
    @ApiOperation(value = "取消订单", notes = "取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO cancelDTO) {
        log.info("管理员订单管理-取消订单{}", cancelDTO);
        Result result = orderService.cancel(cancelDTO);
        return result;
    }

    /**
     * @return com.sky.result.Result
     * @Description: 统计订单数量
     * @author 刘东钦
     * @create 2023/5/3,23:10
     **/
    @GetMapping("/statistics")
    @ApiOperation(value = "统计订单数量", notes = "统计")
    public Result statistics() {
        Result result = orderService.statistics();
        return result;
    }

    /**
     * 完成订单
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,11:04
     **/
    @PutMapping("/complete/{id}")
    @ApiOperation(value = "完成订单", notes = "完成订单")
    public Result complete(@PathVariable("id") Long id) {
        log.info("管理员订单管理-完成订单{}", id);
        Result result = orderService.complete(id);
        return result;
    }

    /**
     * 订单详情
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,11:23
     **/
    @GetMapping("/details/{id}")
    @ApiOperation(value = "订单详情", notes = "订单详情")
    public Result details(@PathVariable("id") Long id) {
        log.info("管理员订单管理-订单详情{}", id);
        Result result = orderService.details(id);
        return result;
    }

    /**
     * 接单
     *
     * @param confirmDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,11:37
     **/
    @PutMapping("/confirm")
    @ApiOperation(value = "确认订单(接单)", notes = "确认订单")
    public Result confirm(@RequestBody OrdersConfirmDTO confirmDTO) {
        log.info("管理员订单管理-确认订单(接单){}", confirmDTO);
        Result result = orderService.confirm(confirmDTO);
        return result;
    }

    /**
     * 拒单
     *
     * @param rejectionDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,12:07
     **/
    @PutMapping("/rejection")
    @ApiOperation(value = "拒绝订单", notes = "拒绝订单")
    public Result rejection(@RequestBody OrdersRejectionDTO rejectionDTO) {
        log.info("管理员订单管理-拒绝订单{}", rejectionDTO);
        Result result = orderService.rejection(rejectionDTO);
        return result;
    }

    /**
     * 发货
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,13:40
     **/
    @PutMapping("/delivery/{id}")
    @ApiOperation(value = "发货", notes = "发货")
    public Result deliver(@PathVariable Long id) {
        log.info("管理员订单管理-发货{}", id);
        Result result = orderService.deliver(id);
        return result;
    }
}
