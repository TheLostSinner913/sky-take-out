package com.sky.aop;

import com.sky.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @Description: aop
 * @Author: 刘东钦
 * @Date: 2023/4/27 8:52
 */
@Aspect
@Slf4j
@Component
public class FillTimeAndUser {
    @Around("@annotation(com.sky.aop.CreateOrUpdate)")
    public Object AutoFill(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取方法上的注解
        Signature signature = joinPoint.getSignature();
        MethodSignature ms= (MethodSignature) signature;
        CreateOrUpdate annotation = ms.getMethod().getAnnotation(CreateOrUpdate.class);
        //获取注解的Value
        FillType value = annotation.value();
        //获取方法字节码文件
        Object[] args = joinPoint.getArgs();
        if (args.length==0||args==null){return joinPoint.proceed();}
        Object arg = args[0];
        Class<?> aClass = arg.getClass();
        //通过反射获得创建和更新的方法
        Method setCreateTime = aClass.getDeclaredMethod("setCreateTime", LocalDateTime.class);
        Method setUpdateTime = aClass.getDeclaredMethod("setUpdateTime", LocalDateTime.class);
        Method setCreateUser = aClass.getDeclaredMethod("setCreateUser", Long.class);
        Method setUpdateUser = aClass.getDeclaredMethod("setUpdateUser", Long.class);
        //如果是创建则
        if (value==FillType.CreateAndUpdate){
            setCreateTime.invoke(arg,LocalDateTime.now());
            setUpdateTime.invoke(arg,LocalDateTime.now());
            setCreateUser.invoke(arg,BaseContext.getCurrentId());
            setUpdateUser.invoke(arg,BaseContext.getCurrentId());
            //如果是更新则
        }else if (value==FillType.OnlyUpdate){
            setUpdateTime.invoke(arg,LocalDateTime.now());
            setUpdateUser.invoke(arg,BaseContext.getCurrentId());
        }
        return joinPoint.proceed();
    }
}
