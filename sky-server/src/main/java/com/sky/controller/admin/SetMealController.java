package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 套餐控制类
 * @Author: 刘东钦
 * @Date: 2023/4/25 22:07
 */
@RestController
@Api(tags = "套餐接口")
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;

    /**
     * 添加套餐
     *
     * @param setmealDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,22:15
     **/
    @ApiOperation(value = "新增套餐")
    @PostMapping
    public Result add(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐{}", setmealDTO);
        Result add = setMealService.add(setmealDTO);
        return add;
    }

    /**
     * 套餐分类查询
     *
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,23:01
     **/
    @GetMapping("/page")
    @ApiOperation(value = "套餐分类查询")
    public Result show(Long categoryId, String name, Integer status,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("套餐分类{},套餐名字包含{},状态{},当前页码{},每页展示{}", categoryId, name, status, page, pageSize);
        Result show = setMealService.show(categoryId, name, status, page, pageSize);
        return show;
    }

    /**
     * 套餐删除
     *
     * @param ids
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,23:25
     **/
    @DeleteMapping
    @ApiOperation(value = "套餐删除")
    public Result delete(Long[] ids) {
        log.info("套餐删除{}", ids);
        Result delete = setMealService.delete(ids);
        return delete;
    }

    /**
     * 根据ID查询套餐
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/26,10:28
     **/
    @GetMapping("/{id}")

    @ApiOperation(value = "根据ID查询套餐")
    public Result getById(@PathVariable Long id) {
        log.info("根据ID查询套餐{}", id);
        Result byId = setMealService.getById(id);
        return byId;
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/26,10:54
     **/
    @PutMapping
    @ApiOperation(value = "套餐修改")
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐{}", setmealDTO);
        Result update = setMealService.update(setmealDTO);
        return update;
    }

    /**
     * 修改套餐启用/停用状态
     * @param status
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/26,14:01
     **/
    @PostMapping("/status/{status}")
    @ApiOperation(value = "修改套餐启用/停用状态")
    public Result OnOrOff(@PathVariable Integer status, Long id) {
        log.info("(套餐)将ID:{},的状态修改为{}", id, status);
        Result status1 = setMealService.status(id, status);
        return status1;
    }
}
