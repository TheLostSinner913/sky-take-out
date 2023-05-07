package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
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
import com.sky.utils.MyHttpClient;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/3 14:15
 */
@Service
@Slf4j
public class UserOrderServiceImpl implements UserOrderService {
    @Autowired
    private UserOrderMapper userOrderMapper;
    @Autowired
    private ShoppingCarMapper shoppingCarMapper;
    @Autowired
    private WebSocketServer webSocketServer;
    @Value("${sky.shop.address}")
    private String shopAddress;
    @Value("${sky.baidu.ak}")
    private String ak;

    //确认距离
    public String distance(String addr) throws IOException, URISyntaxException {
        //商户地址经纬度
        String url = "https://api.map.baidu.com/geocoding/v3";
        HashMap<String, String> hm = new HashMap<>();
        hm.put("address", shopAddress);
        hm.put("output", "json");
        hm.put("ak", ak);
        String s = MyHttpClient.HttpGet(url, hm);
        String lng = JSONObject.parseObject(s).getJSONObject("result").getJSONObject("location").get("lng").toString();
        String lat = JSONObject.parseObject(s).getJSONObject("result").getJSONObject("location").get("lat").toString();
        //用户地址经纬度
        hm.put("address", addr);
        String s1 = MyHttpClient.HttpGet(url, hm);
        String lng1 = JSONObject.parseObject(s1).getJSONObject("result").getJSONObject("location").get("lng").toString();
        String lat1 = JSONObject.parseObject(s1).getJSONObject("result").getJSONObject("location").get("lat").toString();
        //计算距离
        hm.put("origin", lat + "," + lng);
        hm.put("destination", lat1 + "," + lng1);
        hm.put("steps_info", "0");
        String url1 = "https://api.map.baidu.com/directionlite/v1/driving";
        String s2 = MyHttpClient.HttpGet(url1, hm);
        JSONObject jsonObject = JSONObject.parseObject(s2);
        //判断距离是否大于5000米
        String distance = jsonObject.getJSONObject("result").getJSONArray("routes")
                .getJSONObject(0).get("distance").toString();
        log.info("距离为：" + distance);
        return distance;
    }

    /**
     * 用户订单确认
     *
     * @param ordersSubmitDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/3,14:24
     **/
    @Override
    public Result submit(OrdersSubmitDTO ordersSubmitDTO) throws IOException, URISyntaxException {
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
        } else if (Integer.valueOf(distance(address.getDetail())) > 5000) {
            throw new BaseException("超出配送范围");
        }
        //构造Orders信息
        orders.setUserId(userId);
        orders.setStatus(1);
        orders.setPayStatus(0);
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
     *
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

    /**
     * 催单
     * @param id
     * @return void
     * @author 刘东钦
     * @create 2023/5/7,11:43
     **/
    @Override
    public void reminder(Long id) {
        //根据订单id查询订单信息
        Orders order = userOrderMapper.getById(id);
        if (order==null){
            throw new BaseException("订单不存在");
        }
        HashMap hm=new HashMap();
        hm.put("tpye",2);
        hm.put("orderId",id);
        hm.put("content","订单号:"+order.getNumber()+"的订单已催单");
        webSocketServer.sendToAllClient(JSONObject.toJSONString(hm));
    }
}
