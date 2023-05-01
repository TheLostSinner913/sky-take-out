package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex) {
        ex.printStackTrace();
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * SQL语句异常捕获
     * @param sql
     * @return com.sky.result.Result
     * @author 刘东钦
     * @create 2023/4/24,17:17
     **/
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException sql) {
        String msg = sql.getMessage();
        sql.printStackTrace();
        if (msg.contains("Duplicate entry")) {
            String[] split = msg.split(" ");
            String str = split[2];
            return Result.error(str + MessageConstant.ALREADY_EXISTS);
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

}
