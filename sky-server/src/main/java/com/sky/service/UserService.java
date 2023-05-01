package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.result.Result;

/**
 * @Description: 用户Service
 * @Author: 刘东钦
 * @Date: 2023/4/27 15:57
 */
public interface UserService {
    User wxLogin(UserLoginDTO userLoginDTO);

    Result show(Integer type);

    Result getById(Long categoryId);

    Result getId(Long categoryId);
}
