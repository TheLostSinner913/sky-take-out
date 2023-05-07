package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.AdminOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 定时任务
 * @Author: 刘东钦
 * @Date: 2023/5/7 10:29
 */
@Component
@Slf4j
public class MyTask {
    @Autowired
    private AdminOrderMapper orderMapper;

    /**
     * @return void
     * @Description: 自动取消订单(超时支付15分钟)
     * @author 刘东钦
     * @create 2023/5/7,10:35
     **/
    //每十秒自动运行
    @Scheduled(cron = "0/30 * * * * ?")
    public void autoCancelOrder() {
        log.info("定时任务-自动取消订单");
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        Integer status = 1;
        //自动查询超时订单
        List<Orders> orders = orderMapper.checkOverTime(time, status);
        if (orders.size() > 0 || orders != null) {
            //自动取消超时订单
            for (Orders order : orders) {
                order.setCancelTime(LocalDateTime.now());
                order.setStatus(6);
                order.setCancelReason("超时未支付");
                orderMapper.update(order);
            }
        }
    }
}