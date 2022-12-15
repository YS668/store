package com.sms.common;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.sms.common.exception.tokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理类
 */
@ResponseBody
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    public Result handle(Exception e){
        e.printStackTrace();
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(value = tokenException.class)
    public Result tokenHandle(Exception e){
        return Result.tokenError();
    }
}
