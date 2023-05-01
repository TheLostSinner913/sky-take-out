package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 菜品控制类
 * @Author: 刘东钦
 * @Date: 2023/4/25 14:51
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品接口")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,15:58
     **/
    @PostMapping
    public Result add(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品{}", dishDTO);
        dishService.add(dishDTO);
        return Result.success();
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
    @GetMapping("/page")
    @ApiOperation(value = "菜品分页查询")
    public Result show(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, Long categoryId, Integer status) {
        Result show = dishService.show(page, pageSize, name, categoryId, status);
        return show;
    }

    /**
     * 根据ID删除/批量删除菜品
     *
     * @param ids
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,17:50
     **/
    @DeleteMapping
    @ApiOperation(value = "菜品删除")
    public Result deleteById(Long[] ids) {
        log.info("根据ID删除菜品{}", ids);
        Result result = dishService.deleteById(ids);
        return result;
    }

    /**
     * 修改菜品信息
     *
     * @param dishDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,20:25
     **/
    @PutMapping
    @ApiOperation(value = "菜品修改")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品{}", dishDTO);
        Result update = dishService.update(dishDTO);
        return update;
    }

    /**
     * 根据ID查询菜品
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,20:27
     **/
    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询菜品")
    public Result getById(@PathVariable Long id) {
        log.info("根据ID查询菜品{}", id);
        Result byId = dishService.getById(id);
        return byId;
    }

    /**
     * 菜品起售/停售
     *
     * @param status
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,21:16
     **/
    @PostMapping("status/{status}")
    @ApiOperation(value = "菜品起售/停售")
    public Result onOrOff(@PathVariable Integer status, Long id) {
        log.info("菜品{}售卖状态{}", id, status);
        Result result = dishService.onOrOff(id, status);
        return result;
    }

    /**
     * 根据分类ID查询菜品
     * @param categoryId
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,21:48
     **/
    @GetMapping("/list")
    @ApiOperation(value = "根据分类ID查询菜品")
    public Result findById(Long categoryId) {
        log.info("根据分类id{}查询菜品", categoryId);
        Result byId = dishService.findById(categoryId);
        return byId;
    }
}
