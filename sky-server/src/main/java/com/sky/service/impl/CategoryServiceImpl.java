package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.aop.Redis;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:菜品分类实现类
 * @Author: 刘东钦
 * @Date: 2023/4/24 21:59
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 菜品分类分页查询
     * @param name
     * @param page
     * @param pageSize
     * @param type
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,21:57
     **/
    @Override
    @Redis
    public PageResult show(String name, Integer page, Integer pageSize, Integer type) {
        PageHelper.startPage(page,pageSize);
        List<Category> show = categoryMapper.show(name, type);
        Page<Category> p= (Page<Category>) show;
        PageResult pageResult=new PageResult(p.getTotal(),p.getResult());
        return pageResult;
    }
    /**
     * 新增菜品
     *
     * @param categoryDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,22:29
     **/
    @Override
    public Result add(CategoryDTO categoryDTO) {
        //删除redis 缓存
        redisTemplate.delete("category");
        Long currentId = BaseContext.getCurrentId();
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateUser(currentId);
        category.setCreateUser(currentId);
        category.setStatus(1);
        categoryMapper.add(category);
        return Result.success();
    }
    /**
     * 根据ID删除菜品分类
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,22:47
     **/
    @Override
    public Result delete(Long id) {
        categoryMapper.delete(id);
        return Result.success();
    }
    /**
     * 修改菜品启用禁用状态
     * @param status
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,22:56
     **/
    @Override
    public Result statuss(Integer status,Long id) {
        Category category=Category.builder().id(id).status(status)
                .updateTime(LocalDateTime.now()).updateUser(BaseContext.getCurrentId()).build();
        categoryMapper.statusById(category);
        return Result.success();
    }
    /**
     * 修改菜品分类
     * @param categoryDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,23:08
     **/
    @Override
    public Result update(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setUpdateUser(BaseContext.getCurrentId());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.statusById(category);
        return Result.success();
    }
    /**
     * 新增菜品分类拉下框
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,16:15
     * @param type
     **/
    @Override
    public Result choice(Integer type) {
        List<Category> choice = categoryMapper.choice(type);
        return Result.success(choice);
    }
}
