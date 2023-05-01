package com.sky.mapper;

import com.sky.aop.AutoUpdateTimeAndUser;
import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import com.sky.result.Result;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:菜品分类Mapper
 * @Author: 刘东钦
 * @Date: 2023/4/24 21:58
 */
@Mapper
public interface CategoryMapper {
    //分页查询
    List<Category> show(@Param("name") String name, @Param("type") Integer type);
    @AutoUpdateTimeAndUser(OperationType.INSERT)
    //新增分类
    void add(Category category);
    @Delete("delete from category where id=#{id} ")
    //删除分类
    void delete(Long id);
    @AutoUpdateTimeAndUser(OperationType.UPDATE)
    //更新分类
    void statusById(Category category);
    //查询分类
    List<Category> choice(Integer type);
}
