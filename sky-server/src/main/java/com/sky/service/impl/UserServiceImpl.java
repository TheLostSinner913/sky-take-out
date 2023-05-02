package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.*;
import com.sky.exception.BaseException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 用户Service实现类
 * @Author: 刘东钦
 * @Date: 2023/4/27 15:58
 */
@Service
public class UserServiceImpl implements UserService {
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        String openId = getOpenId(userLoginDTO);
        if (openId == null) {
            throw new BaseException(MessageConstant.LOGIN_FAILED);
        }
        User user = userMapper.getByOpenId(openId);
        if (user == null) {
            User user1 = User.builder().openid(openId).createTime(LocalDateTime.now()).build();
            userMapper.add(user1);
        }
        return user;
    }

    /**
     * 用户端查询分类
     *
     * @param type
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/28,16:59
     **/
    @Override
    public Result show(Integer type) {
        List<Category> show = userMapper.show(type);
        return Result.success(show);
    }

    /**
     * 根据分类ID查询菜品及其对应口味
     * @param categoryId
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/28,18:04
     **/
    @Override
    public Result getById(Long categoryId) {
        List<DishVO> dish = userMapper.getDish(categoryId);
        for (DishVO dishVO : dish) {
            Long dishId = dishVO.getId();
            List<DishFlavor> flavors = userMapper.getflaovr(dishId);
            dishVO.setFlavors(flavors);
        }
        return Result.success(dish);
    }

    @Override
    public Result getId(Long categoryId) {
        List<Setmeal> list = userMapper.getId(categoryId);
        return Result.success(list);
    }

    /**
     * 根据套餐ID查询菜品
     * @param id
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/5/2,12:35
     **/
    @Override
    public Result getDishId(Long id) {
        List<DishItemVO> set = userMapper.getDishBySetMealId(id);
        return Result.success(set);
    }

    @Override
    public User practice(String code, String url) {
        HashMap<String,String>hm=new HashMap<>();
        hm.put("appid","wxa9be3e3edf7053bc");
        hm.put("secret","4bd4a3b8fdf269b57b48510f236d741a");
        hm.put("js_code",code);
        hm.put("grant_type","authorization_code");
        String doGet = HttpClientUtil.doGet(url, hm);
        JSONObject jsonObject = JSONObject.parseObject(doGet);
        String openid = jsonObject.get("openid").toString();
        if (openid==null){throw new BaseException("openid不存在");}
        User user = userMapper.getByOpenId(openid);
        if (user==null){
            User u=new User();
            u.setOpenid(openid);
            u.setCreateTime(LocalDateTime.now());
            userMapper.add(u);
            return u;
        }
        return user;
    }

    //调用微信服务API接口获取用户openid
    private String getOpenId(UserLoginDTO userLoginDTO) {
        String code = userLoginDTO.getCode();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("appid", weChatProperties.getAppid());
        hm.put("js_code", code);
        hm.put("grant_type", "authorization_code");
        hm.put("secret", weChatProperties.getSecret());
        String json = HttpClientUtil.doGet(WX_LOGIN, hm);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = (String) jsonObject.get("openid");
        return openid;
    }
}
