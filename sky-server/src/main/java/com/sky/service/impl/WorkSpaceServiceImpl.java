package com.sky.service.impl;

import com.sky.mapper.WorkSpaceMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/7 17:55
 */
@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {
    @Autowired
    private WorkSpaceMapper workSpaceMapper;

    /**
     * 今日运营数据概览
     *
     * @return com.sky.vo.OrderOverViewVO
     * @author 刘东钦
     * @create 2023/5/7,19:16
     **/
    @Override
    public BusinessDataVO businessData(LocalDateTime begin, LocalDateTime end) {
        //获取当天的营业额
        Double turnOver = workSpaceMapper.businessData(begin, end);
        if (turnOver==null){
            turnOver=0.0;
        }
        //获取当天有效订单数(status=5)
        Integer validOrderCount = workSpaceMapper.orderCount(begin, end);
        if (validOrderCount==null){
            validOrderCount=0;
        }
        //获取当天订单完成率
        Integer totalOrderCount = workSpaceMapper.totalOrderCount(begin, end);
        double orderCompletionRate = (double) validOrderCount / totalOrderCount;
        //获取当天平均客单价
        double unitPrice = turnOver / validOrderCount;
        //获取当天新增用户数
        Integer newUsers = workSpaceMapper.newUsers(begin, end);
        BusinessDataVO businessDataVO = BusinessDataVO.builder()
                .turnover(turnOver)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
        return businessDataVO;
    }

    /**
     * 套餐概览
     *
     * @return com.sky.vo.DishOverViewVO
     * @author 刘东钦
     * @create 2023/5/7,18:57
     **/
    @Override
    public DishOverViewVO setMeals() {
        Integer open = workSpaceMapper.openSetMeals();
        Integer close = workSpaceMapper.closeSetMeals();
        DishOverViewVO dishOverViewVO = new DishOverViewVO(open, close);
        return dishOverViewVO;
    }

    /**
     * 菜品概览
     *
     * @return com.sky.vo.OrderOverViewVO
     * @author 刘东钦
     * @create 2023/5/7,19:16
     **/
    @Override
    public  DishOverViewVO dishes() {
        Integer open = workSpaceMapper.openDishes();
        Integer close = workSpaceMapper.closeDishes();
        DishOverViewVO dishOverViewVO = new DishOverViewVO(open, close);
        return dishOverViewVO;
    }

    /**
     * 订单概览
     *
     * @return com.sky.vo.OrderOverViewVO
     * @author 刘东钦
     * @create 2023/5/7,19:16
     **/
    @Override
    public OrderOverViewVO overviewOrders() {
        LocalDateTime begin = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        //待接单数量
        Integer waitingOrders = workSpaceMapper.waitingOrders(begin, end);
        //待派送数量
        Integer deliveredOrders = workSpaceMapper.deliveredOrders(begin, end);
        //已完成数量
        Integer completedOrders = workSpaceMapper.completedOrders(begin, end);
        //已取消数量
        Integer cancelledOrders = workSpaceMapper.cancelledOrders(begin, end);
        //全部订单
        Integer allOrders = workSpaceMapper.allOrders(begin, end);
        OrderOverViewVO orderOverViewVO = new OrderOverViewVO(waitingOrders, deliveredOrders, completedOrders, cancelledOrders, allOrders);
        return orderOverViewVO;
    }
}
