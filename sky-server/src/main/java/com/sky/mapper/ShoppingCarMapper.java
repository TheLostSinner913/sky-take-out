package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.OrderDetail;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @Description: 购物车Mapper
 * @Author: 刘东钦
 * @Date: 2023/5/2 16:31
 */
@Mapper
public interface ShoppingCarMapper {
    //根据菜品ID查询菜品
    @Select("select * from dish where id=#{dishId}")
    Dish getDish(@Param("dishId") Long dishId);
    //购物车添加菜品
    void addDish(ShoppingCart shoppingCart);

    //根据套餐ID查询套餐
    @Select("select * from setmeal where id=#{setmealId} ")
    Setmeal getSetMeal(@Param("setmealId") Long setmealId);

    //购物车添加套餐
    void addSetMeal(ShoppingCart shoppingCart);
    //查询购物车是内是新增行为,还是数量+1
    List<ShoppingCart> check(ShoppingCart shoppingCart);
    //更新购物车内菜品/套餐数量
    void update(ShoppingCart cart);
    @Select("select * from shopping_cart where user_id=#{userId} ")
    List<ShoppingCart> look(Long userId);
    @Delete("delete from shopping_cart where user_id=#{userId} ")
    void delete(Long userId);
    @Delete("delete from shopping_cart where id=#{id}")
    void deleteOne(ShoppingCart cart);

    //分页查询订单历史记录

    //查询历史订单的详细信息
    List<OrderDetail> getByOrderId(@Param("id") Long id);
}
