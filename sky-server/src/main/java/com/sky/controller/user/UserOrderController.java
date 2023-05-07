package com.sky.controller.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.UserOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/3 14:17
 */
@RestController
@Slf4j
@Api(tags = "用户订单管理")
@RequestMapping("/user/order")
public class UserOrderController {
    @Autowired
    private UserOrderService orderService;

    /**
     * 用户订单确认
     *
     * @param ordersSubmitDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/3,14:24
     **/
    @PostMapping("/submit")
    @ApiOperation(value = "用户订单确认", notes = "用户订单确认")
    public Result OrderSubmit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) throws IOException, URISyntaxException {
        log.info("订单确认{}", ordersSubmitDTO);
        Result submit = orderService.submit(ordersSubmitDTO);
        return submit;
    }

    /**
     * 分页查询历史订单
     *
     * @param page
     * @param pageSize
     * @param status
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/3,19:08
     **/
    @GetMapping("/historyOrders")
    @ApiOperation(value = "分页查询历史订单", notes = "分页查询历史订单")
    public Result findOld(@RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          Integer status) {
        log.info("分页查询历史订单{},{},{}", page, pageSize, status);
        Result old = orderService.findOld(page, pageSize, status);
        return old;
    }

    /**
     * 根据订单ID查询订单详情
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/3,21:26
     **/
    @GetMapping("/orderDetail/{id}")
    @ApiOperation(value = "根据订单ID查询订单详情", notes = "根据订单ID查询订单详情")
    public Result findOrderDetail(@PathVariable Long id) {
        log.info("查询订单详情{}", id);
        Result orderDetail = orderService.findOrderDetail(id);
        return orderDetail;
    }

    /**
     * 催单
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,13:55
     **/
    @GetMapping("reminder/{id}")
    @ApiOperation(value = "催单", notes = "催单")
    public Result reminder(@PathVariable Long id) {
        log.info("催单{}", id);
        orderService.reminder(id);
        return Result.success();
    }

    /**
     * 取消订单
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,14:00
     **/
    @PutMapping("cancel/{id}")
    @ApiOperation(value = "取消订单", notes = "取消订单")
    public Result cancel(@PathVariable Long id) {
        log.info("取消订单{}", id);
        Result cancel = orderService.cancel(id);
        return cancel;
    }

    /**
     * 再来一单
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,14:16
     **/
    @PostMapping("/repetition/{id}")
    @ApiOperation(value = "再来一单", notes = "再来一单")
    public Result repetition(@PathVariable Long id) {
        log.info("再来一单{}", id);
        Result again = orderService.again(id);
        return again;
    }
}
