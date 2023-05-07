package com.sky.mapper;

import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/3 14:14
 */
@Mapper
public interface UserOrderMapper {
    //根据用户ID获取 购物车全部信息
    @Select("select * from shopping_cart where user_id=#{userId} ")
    List<ShoppingCart> getAll(Long userId);

    //根据用户ID和 addressBookID 确定用户选择的收获地址信息
    @Select("select * from address_book where user_id=#{userId} and id=#{bookId}")
    AddressBook getAddress(@Param("bookId") Long bookId, @Param("userId") Long userId);
    //提交订单信息
    void submit(Orders orders);
    //提交订单详情
    void details(OrderDetail orderDetail);

    List<OrderVO> show(@Param("status") Integer status, @Param("userId") Long userId);

    @Select("select * from order_detail where order_id=#{id}")
    List<OrderDetail> getByOrderId(Long id);

    //根据订单ID和用户ID获取订单信息
    OrderVO getByOrderIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    //如果状态为1或2 则直接修改订单状态为6(取消)
    Integer cancel(@Param("id") Long id);
    //根据订单id查询订单
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);
}
