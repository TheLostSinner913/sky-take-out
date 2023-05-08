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
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/8 8:55
 */
@Slf4j
@Component
public class TaskDelivered {
    @Autowired
    private AdminOrderMapper orderMapper;
    //20秒执行一次
    @Scheduled(cron = "0/20 * * * * ?")
    public void cancelOverTime(){
        //获得现在时间
        LocalDateTime now = LocalDateTime.now();
        //获取15分钟前
        LocalDateTime time = now.minusMinutes(15);
        List<Orders> orders = orderMapper.againOverTime(time);
        if (orders!=null || orders.size()>0){
            for (Orders order : orders) {
                Long id = order.getId();
            }
        }
    }
}
