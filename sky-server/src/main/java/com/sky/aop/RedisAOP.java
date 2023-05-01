package com.sky.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description: 增删改清除缓存
 * @Author: 刘东钦
 * @Date: 2023/4/29 13:13
 */
@Aspect
@Component
public class RedisAOP {
    @Autowired
    private StringRedisTemplate redisTemplate;
    //增删改清除缓存
    @Around("@annotation(com.sky.aop.Redis)")
    public Object Redis(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取类名截取从开始字母开始到第二个大写字母之间的字符串
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String substring = className.substring(0, 1).toLowerCase() + className.substring(1, 2);
        System.out.println(className);
        System.out.println(substring);
        return joinPoint.proceed();
    }
}
