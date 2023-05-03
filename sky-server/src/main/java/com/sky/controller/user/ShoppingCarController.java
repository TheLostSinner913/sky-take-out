package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCarService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 购物车Controller
 * @Date: 2023/5/2 16:33
 */
@Slf4j
@RestController
@Api(tags = "购物车模块")
@RequestMapping("/user/shoppingCart")
public class ShoppingCarController {
    @Autowired
    private ShoppingCarService shoppingCarService;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,16:40
     **/
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("购物车添加{}", shoppingCartDTO);
        Result add = shoppingCarService.add(shoppingCartDTO);
        return add;
    }

    /**
     * 查看购物车
     *
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,17:50
     **/
    @GetMapping("/list")
    public Result look() {
        log.info("查看购物车");
        List<ShoppingCart> look = shoppingCarService.look();
        return Result.success(look);
    }

    /**
     * 清空购物车
     *
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,22:37
     **/
    @DeleteMapping("/clean")
    public Result delete() {
        log.info("清空购物车");
        shoppingCarService.delete();
        return Result.success();
    }

    /**
     * 购物车减少
     * @param shoppingCartDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,22:58
     **/
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("购物车减少{}", shoppingCartDTO);
        shoppingCarService.sub(shoppingCartDTO);
        return Result.success();
    }
}
