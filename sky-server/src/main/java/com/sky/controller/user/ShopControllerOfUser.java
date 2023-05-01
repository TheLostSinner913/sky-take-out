package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:用户端店铺状态控制类
 * @Author: 刘东钦
 * @Date: 2023/4/26 22:00
 */

@Slf4j
@RestController
@Api(tags = "店铺相关")
@RequestMapping("/user/shop")
public class ShopControllerOfUser {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 用户端店铺状态查询
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/26,22:11
     **/

    @ApiOperation(value = "店铺状态")
    @GetMapping("/status")
    public Result status() {
        String status = (String) redisTemplate.opsForValue().get("status");
        return Result.success(status);
    }
}
