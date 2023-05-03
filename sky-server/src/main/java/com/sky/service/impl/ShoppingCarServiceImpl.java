package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.ShoppingCarMapper;
import com.sky.result.Result;
import com.sky.service.ShoppingCarService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/2 16:33
 */
@Service
public class ShoppingCarServiceImpl implements ShoppingCarService {
    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,16:40
     **/
    @Override
    public Result add(ShoppingCartDTO shoppingCartDTO) {
        //判断购物车里的商品是新增还是数量+1
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //用户只能查询自己的购物车
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> check = shoppingCarMapper.check(shoppingCart);
        if (check.size() == 1 && check != null) {
            //更新数量
            for (ShoppingCart cart : check) {
                cart.setNumber(cart.getNumber() + 1);
                cart.setCreateTime(LocalDateTime.now());
                shoppingCarMapper.update(cart);
            }
        } else {
            //新增
            //判断用户向购物车添加的种类(菜品/套餐)
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();
            String flavor = shoppingCartDTO.getDishFlavor();
            if (dishId != null) {
                //如果是菜品,查询该菜品ID
                Dish dish = shoppingCarMapper.getDish(dishId);
                ShoppingCart shoppingCartNew = ShoppingCart.builder()
                        .dishFlavor(flavor)
                        .dishId(dishId)
                        .image(dish.getImage())
                        .amount(dish.getPrice())
                        .createTime(LocalDateTime.now())
                        .userId(BaseContext.getCurrentId())
                        .name(dish.getName()).build();
                shoppingCarMapper.addDish(shoppingCartNew);
            } else {
                //如果是套餐,查询套餐ID
                Setmeal setMeal = shoppingCarMapper.getSetMeal(setmealId);
                ShoppingCart shoppingCartNew = ShoppingCart.builder()
                        .dishFlavor(flavor)
                        .name(setMeal.getName())
                        .amount(setMeal.getPrice())
                        .image(setMeal.getImage())
                        .createTime(LocalDateTime.now())
                        .userId(BaseContext.getCurrentId())
                        .setmealId(setmealId).build();
                shoppingCarMapper.addSetMeal(shoppingCartNew);
            }
        }
        return Result.success();
    }

    /**
     * 查看购物车
     *
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,17:50
     **/
    @Override
    public List<ShoppingCart> look() {
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> look = shoppingCarMapper.look(userId);
        return look;
    }

    /**
     * 清空购物车
     * @return void
     * @author 刘东钦
     * @create 2023/5/2,22:58
     **/
    @Override
    public void delete() {
        Long userId = BaseContext.getCurrentId();
        shoppingCarMapper.delete(userId);
    }

    /**
     * 购物车减少
     *
     * @param shoppingCartDTO
     * @return void
     * @author 刘东钦
     * @create 2023/5/2,22:58
     **/
    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        List<ShoppingCart> check = shoppingCarMapper.check(shoppingCart);
        for (ShoppingCart cart : check) {
            if (cart.getNumber() > 1) {
                cart.setNumber(cart.getNumber() - 1);
                shoppingCarMapper.update(cart);
            } else if (cart.getNumber() == 1) {
                shoppingCarMapper.deleteOne(cart);
            }
        }
    }
}
