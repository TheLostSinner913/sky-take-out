package com.sky.controller.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.UserOrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result OrderSubmit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
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
    public Result findOld(@RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          Integer status) {
        log.info("分页查询历史订单{},{},{}", page, pageSize, status);
        Result old = orderService.findOld(page, pageSize, status);
        System.out.println(old);
        return old;
    }

    /**
     * 根据订单ID查询订单详情
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/3,21:26
     **/
    @GetMapping("/orderDetail/{id}")
    public Result findOrderDetail(@PathVariable Long id) {
        log.info("查询订单详情{}", id);
        Result orderDetail = orderService.findOrderDetail(id);
        return orderDetail;
    }


}
