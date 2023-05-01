package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;

/**
 * @Description:菜品分类Service
 * @Author: 刘东钦
 * @Date: 2023/4/24 21:59
 */
public interface CategoryService {

    PageResult show(String name, Integer page, Integer pageSize, Integer type);

    Result add(CategoryDTO categoryDTO);

    Result delete(Long id);

    Result statuss(Integer status,Long id);

    Result update(CategoryDTO categoryDTO);

    Result choice(Integer type);
}
