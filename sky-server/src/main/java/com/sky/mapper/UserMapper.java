package com.sky.mapper;

import com.sky.entity.*;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserMapper {
    @Select("select * from user where openid=#{openId} ")
    User getByOpenId(String openId);
    @Insert("insert into user(openid,create_time)values (#{openid},#{createTime})")
    Integer add(User user);

    List<Category> show(@Param("type") Integer type);
    @Select("select * from dish d ,category c where d.category_id=#{categoryId} ")
    List<DishVO> getById(Long categoryId);

    List<Setmeal> getId(@Param("categoryId") Long categoryId);

    List<DishVO> getDish(@Param("categoryId") Long categoryId);

    //根据菜品id查询口味
    List<DishFlavor> getflaovr(Long dishId);

    //根据套餐id查询 菜品信息
    List<DishItemVO> getDishBySetMealId(@Param("id") Long id);
}
