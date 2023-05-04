package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.OrderDetail;
import com.sky.exception.BaseException;
import com.sky.mapper.AdminOrderMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${sky.shop.address}")
    private String shopAddress;
    @Value("${sky.baidu.ak}")
    private String ak;

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
     * @param cancelDTO
     * @return com.sky.result.Result
     * @Description: 取消订单
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
     * @return com.sky.result.Result
     * @Description: 统计订单数量
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

    /**
     * 完成订单
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,11:04
     **/
    @Override
    public Result complete(Long id) {
        Integer complete = adminOrderMapper.complete(id);
        if (complete == 0) {
            throw new BaseException("订单不存在");
        }
        return Result.success();
    }

    /**
     * 订单详情
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,11:23
     **/
    @Override
    public Result details(Long id) {
        //根据id查询订单信息
        OrderVO orderVO = adminOrderMapper.msg(id);
        //根据id查询订单详细信息
        List<OrderDetail> orderDetails = adminOrderMapper.orderDetail(id);
        StringBuilder sb=new StringBuilder();
        for (OrderDetail detail : orderDetails) {
            //拼接name和number
            sb.append(detail.getName()).append("x").append(detail.getNumber()).append(",");
        }
        orderVO.setOrderDetailList(orderDetails);
        orderVO.setOrderDishes(sb.toString());
        return Result.success(orderVO);
    }
    /**
     * 接单
     * @param confirmDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,11:37
     **/
    @Override
    public Result confirm(OrdersConfirmDTO confirmDTO) {
        Long id = confirmDTO.getId();
        Integer status = confirmDTO.getStatus();
        Integer confirm = adminOrderMapper.confirm(id,status);
        if (confirm==0){
            throw new BaseException("订单不存在");
        }
        return Result.success();
    }
    /**
     * 拒单
     * @param rejectionDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,12:07
     **/
    @Override
    public Result rejection(OrdersRejectionDTO rejectionDTO) {
        Long id = rejectionDTO.getId();
        String rejectionReason = rejectionDTO.getRejectionReason();
        LocalDateTime cancelTime = LocalDateTime.now();
        Integer reject = adminOrderMapper.reject(rejectionReason, id, cancelTime);
        if (reject==0){
            throw new BaseException("无法拒单");
        }
        //todo 如果是已支付状态，需要退款
        return Result.success();
    }
    /**
     * 发货
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,13:40
     **/
    @Override
    public Result deliver(Long id) {
        Integer deliver = adminOrderMapper.deliver(id);
        if (deliver==0){
            throw new BaseException("无法发货");
        }
        return Result.success();
    }
}
