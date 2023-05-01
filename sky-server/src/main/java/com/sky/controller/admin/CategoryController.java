package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 菜品分类控制类
 * @Author: 刘东钦
 * @Date: 2023/4/24 21:53
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "菜品分类相关接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 菜品分类分页查询
     *
     * @param name
     * @param page
     * @param pageSize
     * @param type
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,21:57
     **/
    @GetMapping("/page")
    @ApiOperation(value = "菜品分类查询")
    public Result show(String name, @RequestParam(defaultValue = "1") Integer page
            , @RequestParam(defaultValue = "5") Integer pageSize, Integer type) {
        PageResult show = categoryService.show(name, page, pageSize, type);
        return Result.success(show);
    }

    /**
     * 新增菜品
     *
     * @param categoryDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,22:29
     **/
    @ApiOperation(value = "菜品新增")
    @PostMapping
    public Result add(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类{}", categoryDTO);
        Result add = categoryService.add(categoryDTO);
        return add;
    }

    /**
     * 根据ID删除菜品分类
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,22:47
     **/
    @ApiOperation(value = "根据菜品id删除")
    @DeleteMapping
    public Result delete(Long id) {
        log.info("根据ID删除分类{}", id);
        Result delete = categoryService.delete(id);
        return delete;
    }

    /**
     * 修改菜品启用禁用状态
     *
     * @param status
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,22:56
     **/
    @ApiOperation(value = "启用/禁用分类")
    @PostMapping("/status/{status}")
    public Result onOrOFF(@PathVariable Integer status, Long id) {
        log.info("{}菜品分类启用/禁用{}", id, status);
        categoryService.statuss(status, id);
        return Result.success();
    }

    /**
     * 修改菜品分类
     * @param categoryDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,23:08
     **/
    @ApiOperation(value = "修改分类")
    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }
    /**
     * 新增菜品分类拉下框
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,16:15
     * @param type
     **/
    @GetMapping("/list")
    @ApiOperation(value = "新增菜品分类拉下框")
    public Result choice(Integer type){
        Result choice = categoryService.choice(type);
        return choice;
    }
}
