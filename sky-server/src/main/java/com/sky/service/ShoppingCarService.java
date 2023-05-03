package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;

import java.util.List;

/**
 * @Description: 购物车Service
 * @Author: 刘东钦
 * @Date: 2023/5/2 16:32
 */
public interface ShoppingCarService {
    Result add(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> look();

    void delete();

    void sub(ShoppingCartDTO shoppingCartDTO);
}
