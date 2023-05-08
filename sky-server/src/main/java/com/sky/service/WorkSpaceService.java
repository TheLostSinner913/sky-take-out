package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/7 17:55
 */
public interface WorkSpaceService {
    BusinessDataVO businessData(LocalDateTime begin, LocalDateTime end);

    DishOverViewVO setMeals();

    DishOverViewVO dishes();

    OrderOverViewVO overviewOrders();

}
