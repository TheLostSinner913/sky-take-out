package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.OrdersCancelDTO;
import com.sky.entity.OrderDetail;
import com.sky.mapper.AdminOrderMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/3 21:46
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private AdminOrderMapper adminOrderMapper;

    @Override
    public Result conditionSearch(Integer page, Integer pageSize, LocalDateTime beginTime, LocalDateTime endTime, Integer number, String phone, Integer status) {
        PageHelper.startPage(page, pageSize);
        List<OrderVO> orderVOS = adminOrderMapper.conditionSearch(beginTime, endTime, number, phone, status);
        for (OrderVO orderVO : orderVOS) {
            Long orderId = orderVO.getId();
            //根据orderId查询订单详情
            List<OrderDetail> orderDetails = adminOrderMapper.orderDetail(orderId);
            //将订单菜品信息name 和number 拼接
            StringBuilder sb = new StringBuilder();
            for (OrderDetail detail : orderDetails) {
                sb.append(detail.getName()).append("x").append(detail.getNumber()).append(",");
            }
            String orderDishes = sb.toString();
            orderVO.setOrderDishes(orderDishes);
            orderVO.setOrderDetailList(orderDetails);
        }
        Page<OrderVO> p = (Page<OrderVO>) orderVOS;
        PageResult pageResult = new PageResult(p.getTotal(), p.getResult());
        return Result.success(pageResult);
    }

    /**
     * @Description: 取消订单
     * @param cancelDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/3,22:52
     **/
    @Override
    public Result cancel(OrdersCancelDTO cancelDTO) {
        Long orderId = cancelDTO.getId();
        String cancelReason = cancelDTO.getCancelReason();
        LocalDateTime cancelTime = LocalDateTime.now();
        adminOrderMapper.cancel(orderId, cancelReason, cancelTime);
            //如果是已支付状态，需要退款
            //TODO
        return Result.success();
    }
    /**
     * @Description: 统计订单数量
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/3,23:10
     **/
    @Override
    public Result statistics() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        //统计待接单数量
        Integer a = adminOrderMapper.toBeConfirmed();
        //统计待派送数量
        Integer b = adminOrderMapper.confirmed();
        //统计派送中数量
        Integer c = adminOrderMapper.deliveryInProgress();
        orderStatisticsVO.setToBeConfirmed(a);
        orderStatisticsVO.setConfirmed(b);
        orderStatisticsVO.setDeliveryInProgress(c);
        return Result.success(orderStatisticsVO);
    }
}
