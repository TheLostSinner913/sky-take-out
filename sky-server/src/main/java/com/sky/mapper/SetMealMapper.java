package com.sky.mapper;

import com.sky.aop.AutoUpdateTimeAndUser;
import com.sky.aop.CreateOrUpdate;
import com.sky.aop.FillType;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 套餐Mapper
 * @Author: 刘东钦
 * @Date: 2023/4/25 22:10
 */
@Mapper
public interface SetMealMapper {
    //添加套餐
    @CreateOrUpdate(FillType.CreateAndUpdate)
    void addMeal(Setmeal setmeal);
    //添加套餐对应的菜品信息
    void addDish(SetmealDish setmealDish);

    //分页查询
    List<SetmealVO> show(@Param("categoryId") Long categoryId, @Param("name") String name, @Param("status") Integer status);

    //套餐删除
    Integer delete(@Param("ids") Long[] ids);

    //删除套餐菜品关系表的逻辑外键
    void deleteSD(@Param("ids") Long[] ids);
    //根据ID查询套餐
    SetmealVO getById(Long id);
    //根据上述ID查询套餐菜品对应关系
    List<SetmealDish> getSD(Long id);
    //修改才套餐信息
    @AutoUpdateTimeAndUser(OperationType.UPDATE)
    void update(Setmeal setmeal);
    //删除原套餐关联菜品
    void deleteOld(Long setMealId);
    //查询套餐关联的菜品是否处于停售状态
    List<String> checkFromDish(Long id);

    //套餐起售/停售
    void OnOrOff(@Param("id") Long id, @Param("status") Integer status);
}
