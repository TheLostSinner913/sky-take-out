package com.sky.interceptor;

import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 用户JWT令牌拦截
 * @Author: 刘东钦
 * @Date: 2023/4/27 19:53
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)){return true;}
        String token = request.getHeader("authentication");
        try {
            Claims claims = JwtUtil.parseJWT("itheima",token);
            String str = claims.get("id").toString();
            Long userId= Long.valueOf(str);
            BaseContext.setCurrentId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
