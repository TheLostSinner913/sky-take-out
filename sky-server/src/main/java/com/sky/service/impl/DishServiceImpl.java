package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.BaseException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 菜品Service实现
 * @Author: 刘东钦
 * @Date: 2023/4/25 14:54
 */
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,17:21
     **/
    @Override
    @Transactional
    public Result add(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.add(dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        for (int i = 0; i < flavors.size(); i++) {
            flavors.get(i).setDishId(dish.getId());
            dishMapper.addFlavor(flavors.get(i));
        }
        return null;
    }

    /**
     * 菜品分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @param categoryId
     * @param status
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,17:21
     **/
    @Override
    public Result show(Integer page, Integer pageSize, String name, Long categoryId, Integer status) {
        PageHelper.startPage(page, pageSize);
        List<DishVO> show = dishMapper.show(name, categoryId, status);
        Page<DishVO> p = (Page<DishVO>) show;
        PageResult pageResult = new PageResult(p.getTotal(), p.getResult());
        return Result.success(pageResult);
    }

    /**
     * 根据ID删除/批量删除菜品
     *
     * @param ids
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,17:50
     **/
    @Override
    @Transactional
    public Result deleteById(Long[] ids) {
        //查询套餐分类
        List<Dish> check = dishMapper.check(ids);
        if (check.size() != 0) {
            throw new BaseException("菜品被套餐关联,无法删除");
        } else {
            Integer i = dishMapper.deleteById(ids);
            if (i == 0) {
                throw new BaseException("起售状态的菜品无法删除");
            } else if (i < ids.length) {
                throw new BaseException("批量删除中部分商品处于起售状态无法删除");

            } else dishMapper.deleteFlavor(ids);
        }
        return Result.success();
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,20:41
     **/
    @Override
    @Transactional
    public Result update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //删除该ID对应 原有口味
        dishMapper.deleteOld(dish);
        //添加新口味
        for (int i = 0; i < flavors.size(); i++) {
            flavors.get(i).setDishId(dish.getId());
            dishMapper.addNewFlavor(flavors.get(i));
        }
        return Result.success();
    }

    /**
     * 根据ID查询菜品
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,20:27
     **/
    @Override
    public Result getById(Long id) {
        DishVO dishVO = dishMapper.getById(id);
        List<DishFlavor> flavor = dishMapper.getFlavor(id);
        dishVO.setFlavors(flavor);
        return Result.success(dishVO);
    }

    /**
     * 菜品起售/停售
     *
     * @param status
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,21:16
     **/
    @Override
    public Result onOrOff(Long id, Integer status) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        //如果将菜品停售(原为起售)
        if (status == 0) {
            //根据菜品ID查询相关联的套餐ID
            List<Long> longs = dishMapper.selectSetMeal(id);
            if (longs.size()==0){dishMapper.update(dish);}
            else {
                //将关联的套餐转换为停售状态
                dishMapper.OnOrOff(longs);
                //将菜品进行状态更新
                dishMapper.update(dish);
            }
            //如果将菜品起售 则直接执行菜品状态更新
        } else if (status == 1) {
            dishMapper.update(dish);
        }
        return Result.success();
    }

    /**
     * 根据分类ID查询菜品
     *
     * @param categoryId
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,21:48
     **/
    @Override
    public Result findById(Long categoryId) {
        List<DishVO> byId = dishMapper.findById(categoryId);
        return Result.success(byId);
    }
}
