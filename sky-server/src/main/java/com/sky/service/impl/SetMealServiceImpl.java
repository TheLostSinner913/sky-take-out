package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 套餐实现类
 * @Author: 刘东钦
 * @Date: 2023/4/25 22:12
 */
@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * 添加套餐
     *
     * @param setmealDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,22:15
     **/
    @Override
    public Result add(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //默认停售状态
        setmeal.setStatus(0);
        setMealMapper.addMeal(setmeal);
        List<SetmealDish> sd = setmealDTO.getSetmealDishes();
        for (int i = 0; i < sd.size(); i++) {
            //逻辑外键
            sd.get(i).setSetmealId(setmeal.getId());
            setMealMapper.addDish(sd.get(i));
        }
        return Result.success();
    }

    /**
     * 套餐分类查询
     *
     * @param categoryId
     * @param name
     * @param status
     * @param page
     * @param pageSize
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,23:07
     **/
    @Override
    public Result show(Long categoryId, String name, Integer status, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<SetmealVO> show = setMealMapper.show(categoryId, name, status);
        Page<SetmealVO> p = (Page<SetmealVO>) show;
        PageResult pageResult = new PageResult(p.getTotal(), p.getResult());
        return Result.success(pageResult);
    }

    /**
     * 套餐删除
     *
     * @param ids
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/25,23:25
     **/
    @Override
    @Transactional
    public Result delete(Long[] ids) {
        Integer delete = setMealMapper.delete(ids);
        if (delete == 0) {
            throw new BaseException("套餐为起售状态,无法删除");
        }
        //删除套餐菜品对应表中关系
        setMealMapper.deleteSD(ids);
        return Result.success();
    }

    /**
     * 根据ID查询套餐
     *
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/26,10:28
     **/
    @Override
    public Result getById(Long id) {
        SetmealVO setmealVO = setMealMapper.getById(id);
        List<SetmealDish> sd = setMealMapper.getSD(id);
        setmealVO.setSetmealDishes(sd);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/26,10:55
     **/
    @Override
    @Transactional
    public Result update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //修改套餐
        setMealMapper.update(setmeal);
        //修改套餐对应菜品
        List<SetmealDish> sd = setmealDTO.getSetmealDishes();
        Long setMealId = setmealDTO.getId();
        //删除老的关联菜品
        setMealMapper.deleteOld(setMealId);
        for (int i = 0; i < sd.size(); i++) {
            sd.get(i).setSetmealId(setMealId);
            //插入新的关联菜品
            setMealMapper.addDish(sd.get(i));
        }
        return Result.success();
    }
    /**
     * 修改套餐启用/停用状态
     * @param status
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/26,14:01
     **/
    @Override
    public Result status(Long id, Integer status) {
        if (status==1){
            //查询套餐关联的菜品是否处于停售状态
            List<String> list = setMealMapper.checkFromDish(id);
            if (list.size()==0){
                //开启起售状态
                setMealMapper.OnOrOff(id,status);
            }else {throw new  BaseException("套餐中关联的菜品"+list+"处于停售状态");}
        }
        setMealMapper.OnOrOff(id,status);
        return Result.success();
    }



}
