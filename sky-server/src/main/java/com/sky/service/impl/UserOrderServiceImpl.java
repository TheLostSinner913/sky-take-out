package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.BaseException;
import com.sky.mapper.ShoppingCarMapper;
import com.sky.mapper.UserOrderMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.UserOrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/3 14:15
 */
@Service
public class UserOrderServiceImpl implements UserOrderService {
    @Autowired
    private UserOrderMapper userOrderMapper;
    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    /**
     * 用户订单确认
     *
     * @param ordersSubmitDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/3,14:24
     **/
    @Override
    public Result submit(OrdersSubmitDTO ordersSubmitDTO) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        Long userId = BaseContext.getCurrentId();
        //根据userId获取该用户的购物车信息
        List<ShoppingCart> shoppingCarts = userOrderMapper.getAll(userId);
        //根据addressBookID获取用户选择的收货地址 和收货信息
        Long bookId = ordersSubmitDTO.getAddressBookId();
        AddressBook address = userOrderMapper.getAddress(bookId, userId);
        if (address == null) {
            throw new BaseException("请填写收货地址");
        }
        //构造Orders信息
        orders.setUserId(userId);
        orders.setStatus(1);
        orders.setPayStatus(Orders.PENDING_PAYMENT);
        orders.setPhone(address.getPhone());
        orders.setAddress(address.getDetail());
        orders.setConsignee(address.getConsignee());
        orders.setOrderTime(LocalDateTime.now());
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        userOrderMapper.submit(orders);
        //构建OrderDetial信息
        for (ShoppingCart cart : shoppingCarts) {
            //将购物车信息赋值给 订单详情
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            //添加订单详情
            userOrderMapper.details(orderDetail);
        }
        //清空购物车
        shoppingCarMapper.delete(BaseContext.getCurrentId());
        //构造返回结果VO
        OrderSubmitVO submitVO = new OrderSubmitVO();
        submitVO.setOrderTime(orders.getOrderTime());
        submitVO.setId(orders.getId());
        submitVO.setOrderAmount(orders.getAmount());
        submitVO.setOrderNumber(orders.getNumber());
        return Result.success(submitVO);
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
    @Override
    public Result findOld(Integer page, Integer pageSize, Integer status) {
        PageHelper.startPage(page, pageSize);
        Long userId = BaseContext.getCurrentId();
        //根据用户ID 和status查询订单表(缺少orderDetails)
        List<OrderVO> show = userOrderMapper.show(status, userId);
        for (OrderVO orderVO : show) {
            //获取orderID
            Long orderId = orderVO.getId();
            //根据orderID 获取 details集合
            List<OrderDetail> details = userOrderMapper.getByOrderId(orderId);
            orderVO.setOrderDetailList(details);
        }
        Page<OrderVO> p = (Page<OrderVO>) show;
        PageResult pageResult = new PageResult(p.getTotal(), p.getResult());
        return Result.success(pageResult);
    }

    /**
     * 根据订单Id(orderID)查询订单详情(orderDetails)
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/3,21:26
     **/
    @Override
    public Result findOrderDetail(Long id) {
        Long userId = BaseContext.getCurrentId();
        //根据orderID 获取 details集合
        List<OrderDetail> details = userOrderMapper.getByOrderId(id);
        //根据orderId 和 userId 获取订单信息
        OrderVO orderVO = userOrderMapper.getByOrderIdAndUserId(id, userId);
        orderVO.setOrderDetailList(details);
        return Result.success(orderVO);
    }

    /**
     * 取消订单
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,14:00
     **/
    @Override
    public Result cancel(Long id) {
        Long userId = BaseContext.getCurrentId();
        //根据ID 和 userId 获取订单信息
        OrderVO orderVO = userOrderMapper.getByOrderIdAndUserId(id, userId);
        //如果订单状态为待付款或待接单状态->直接取消
        if (orderVO.getStatus() == 1 || orderVO.getStatus() == 2) {
            userOrderMapper.cancel(id);
            return Result.success();
            //如果订单状态为已接单或派送中,则抛出异常需要联系客服
        } else if (orderVO.getStatus() == 3) {
            throw new BaseException("订单已接单,请联系客服");
        } else if (orderVO.getStatus() == 4) {
            throw new BaseException("订单派送中,请联系客服");
            //如果订单状态为待接单,需修改状态为6,并退款
        } else if (orderVO.getStatus() == 5) {
            userOrderMapper.cancel(id);
            //todo 退款
        }
        return Result.success();
    }

    /**
     * 再来一单
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/4,14:17
     **/
    @Override
    public Result again(Long id) {
        //将订单为id的商品添加到购物车
        Long userId = BaseContext.getCurrentId();
        //根据userId 和id查询订单详情
        List<OrderDetail> details = userOrderMapper.getByOrderId(id);
        for (OrderDetail detail : details) {
            //将订单详情信息添加到购物车
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(detail, shoppingCart);
            shoppingCart.setUserId(userId);
            shoppingCarMapper.add(shoppingCart);
        }
        return Result.success();
    }
}
