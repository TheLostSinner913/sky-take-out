package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 服务端店铺状态控制类
 * @Author: 刘东钦
 * @Date: 2023/4/26 21:39
 */
@RestController
@Slf4j
@Api(tags = "店铺相关")
@RequestMapping("/admin/shop")
public class ShopController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 设置店铺是否打烊
     * @param status
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/26,21:44
     **/
    @ApiOperation(value = "设置是否打烊")
    @PutMapping("/{status}")
    public Result OpenClose(@PathVariable Integer status) {
        stringRedisTemplate.opsForValue().set("status",status.toString());
        return Result.success();
    }
    @ApiOperation(value = "查看是否打烊")
    @GetMapping("/status")
    public Result check(){
        Integer status = Integer.valueOf(stringRedisTemplate.opsForValue().get("status"));
        return Result.success(status);
    }

}
