package com.tencent.wxcloudrun.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        logger.error("系统异常", e);
        return ApiResponse.error("系统异常: " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse handleRuntimeException(RuntimeException e) {
        logger.error("业务异常", e);
        return ApiResponse.error(e.getMessage());
    }
}

