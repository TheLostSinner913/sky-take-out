package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/3 14:15
 */
public interface UserOrderService {
    Result submit(OrdersSubmitDTO ordersSubmitDTO) throws IOException, URISyntaxException;

    Result findOld(Integer page, Integer pageSize, Integer status);

    Result findOrderDetail(Long id);

    Result cancel(Long id);

    Result again(Long id);

    void reminder(Long id);
}
