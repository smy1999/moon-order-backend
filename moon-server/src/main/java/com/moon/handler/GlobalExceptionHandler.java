package com.moon.handler;

import com.moon.constant.MessageConstant;
import com.moon.exception.BaseException;
import com.moon.result.Result;
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
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result<String> exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获SQL异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result<String> sqlUniqueExceptionHandler(SQLIntegrityConstraintViolationException ex) {
        // Duplicate entry 'zs' for key 'employee.idx_username'
        String message = ex.getMessage();
        log.error("异常信息：{}", message);
        if (message.contains("Duplicate entry")) {
            String returnMessage = message.split("'")[1] + MessageConstant.ALREADY_EXISTS;
            return Result.error(returnMessage);
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

}
