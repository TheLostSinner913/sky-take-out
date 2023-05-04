package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
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
}
