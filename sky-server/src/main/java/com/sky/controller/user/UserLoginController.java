package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @Description: 微信用户登录控制类
 * @Author: 刘东钦
 * @Date: 2023/4/27 15:48
 */
@RestController
@Slf4j
@Api(tags = "微信用户相关接口")
@RequestMapping("/user")
public class UserLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 微信登陆小程序
     *
     * @param userLoginDTO
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/28,16:56
     **/
    @PostMapping("/user/login")
    @ApiOperation(value = "微信登录")
    public Result userLogin(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("微信用户登录{}", userLoginDTO);
        //调用Service层获取User对象
        User user = userService.wxLogin(userLoginDTO);
        System.out.println(user);
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), hm);
        UserLoginVO userLoginVO = UserLoginVO.builder().token(token).openid(user.getOpenid()).id(user.getId()).build();
        return Result.success(userLoginVO);
    }

    /**
     * 用户端查询分类
     *
     * @param type
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/28,16:59
     **/
    @GetMapping("/category/list")
    @ApiOperation(value = "查询分类")
    public Result show(Integer type) {
        Result show = userService.show(type);
        return show;
    }

    /**
     * 根据分类ID查询菜品
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/28,18:03
     **/
    @GetMapping("/shoppingCart/list")
    @ApiOperation(value = "根据分类ID查询菜品")
    public Result getById(Long categoryId) {
        log.info("根据分类ID查询菜品{}",categoryId);
        Result byId = userService.getById(categoryId);
        return byId;
    }

    @GetMapping("/setmeal/list")
    public Result getId(Long categoryId){
        Result id = userService.getId(categoryId);
        return id;
    }
}
