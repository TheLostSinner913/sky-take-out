package com.sky.service;

import com.sky.dto.OrdersCancelDTO;
import com.sky.result.Result;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/3 21:46
 */
public interface OrderService {

    Result conditionSearch(Integer page, Integer pageSize, LocalDateTime beginTime, LocalDateTime endTime, Integer number, String phone, Integer status);

    Result cancel(OrdersCancelDTO cancelDTO);

    Result statistics();

}
