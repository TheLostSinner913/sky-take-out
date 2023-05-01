package com.sky.aop;

import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @Description: AOP
 * @Author: 刘东钦
 * @Date: 2023/4/24 20:18
 */
@Aspect
@Slf4j
@Component
public class AutoFill {
    @Around("@annotation(com.sky.aop.AutoUpdateTimeAndUser)")
    public Object updateTimeAndUserId(ProceedingJoinPoint joinPoint) throws Throwable {
        log.warn("aop进入方法准备进行增强");
        //获得方法签名
        Signature signature = joinPoint.getSignature();
        //将签名类型转换为 MethodSignature类型
        MethodSignature signatureM= (MethodSignature) signature;
        //调用方法获得该方法上的注解对象
        AutoUpdateTimeAndUser annotation = signatureM.getMethod().getAnnotation(AutoUpdateTimeAndUser.class);
        //获取注解的value值
        OperationType operationType = annotation.value();
        //获得方法的参数
        Object[] args = joinPoint.getArgs();
        //对参数做非空判断
        if (args==null || args.length==0){return joinPoint.proceed();}
        Object arg = args[0];
        //反射
        Class<?> aClass = arg.getClass();
        Method setCreateTime = aClass.getDeclaredMethod("setCreateTime", LocalDateTime.class);
        Method setUpdateTime = aClass.getDeclaredMethod("setUpdateTime", LocalDateTime.class);
        Method setCreateUser = aClass.getDeclaredMethod("setCreateUser", Long.class);
        Method setUpdateUser = aClass.getDeclaredMethod("setUpdateUser", Long.class);
        if (operationType==OperationType.INSERT){
            setCreateTime.invoke(arg,LocalDateTime.now());
            setUpdateTime.invoke(arg,LocalDateTime.now());
            setCreateUser.invoke(arg,BaseContext.getCurrentId());
            setUpdateUser.invoke(arg,BaseContext.getCurrentId());
        }else if (operationType==OperationType.UPDATE){
            setUpdateTime.invoke(arg,LocalDateTime.now());
            setUpdateUser.invoke(arg,BaseContext.getCurrentId());
        }
        Object proceed = joinPoint.proceed();
        return proceed;
    }
}
