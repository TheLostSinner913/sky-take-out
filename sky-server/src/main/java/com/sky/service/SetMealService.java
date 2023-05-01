package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;

/**
 * @Description: 套餐Service
 * @Author: 刘东钦
 * @Date: 2023/4/25 22:11
 */
public interface SetMealService {

    Result add(SetmealDTO setmealDTO);

    Result show(Long categoryId, String name, Integer status, Integer page, Integer pageSize);

    Result delete(Long[] ids);

    Result getById(Long id);

    Result update(SetmealDTO setmealDTO);

    Result status(Long id, Integer status);
}
