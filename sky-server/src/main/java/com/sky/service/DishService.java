package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.result.Result;

/**
 * @Description: 菜品Service
 * @Author: 刘东钦
 * @Date: 2023/4/25 14:54
 */
public interface DishService {
    Result add(DishDTO dishDTO);

    Result show(Integer page, Integer pageSize, String name, Long categoryId, Integer status);

    Result deleteById(Long[] ids);

    Result update(DishDTO dishDTO);

    Result getById(Long id);

    Result onOrOff(Long id ,Integer status);

    Result findById(Long id);
}
