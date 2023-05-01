package com.sky.mapper;

import com.sky.aop.AutoUpdateTimeAndUser;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {
    //新增菜品
    @AutoUpdateTimeAndUser(OperationType.INSERT)
    void add(Dish dish);
    //添加菜品对应的口味
    void addFlavor(DishFlavor dishFlavor);

    //菜品分页查询
    List<DishVO> show(@Param("name") String name, @Param("categoryId") Long categoryId, @Param("status") Integer status);
    //查询要删除的菜品是否关联套餐,返回的集合长度为0则不关联
    List<Dish> check(@Param("ids") Long[] ids);

    //根据菜品ID删除(起售无法删除)
    Integer deleteById(@Param("ids") Long[] ids);

    //删除菜品对应的口味
    void deleteFlavor(@Param("ids") Long[] ids);

    //根据ID查询菜品
    @Select("select * from dish where id=#{id}")
    DishVO getById(@Param("id") Long id);
    //根据ID查询口味集合
    @Select("select * from dish_flavor where dish_id=#{id} ")
    List<DishFlavor> getFlavor(Long id);

    //更新菜品信息
    @AutoUpdateTimeAndUser(OperationType.UPDATE)
    void update(Dish dish);
    //删除原有口味
    @Options(keyProperty = "id",useGeneratedKeys = true)
    Integer deleteOld(Dish dish);
    //添加新口味
    void addNewFlavor(DishFlavor dishFlavor);
    //根据分类查询菜品
    @Select("select * from dish where category_id=#{categoryId} ")
    List<DishVO> findById(Long categoryId);

    //根据菜品id查询相关起售的套餐
    List<Setmeal> select(@Param("id") Long id);

    //根据菜品ID查询相关套餐ID
    List<Long> selectSetMeal(@Param("id") Long id);

    //将相关套餐变为停售
    Integer OnOrOff(@Param("longs") List<Long> longs);
}
