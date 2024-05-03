package com.til.water_detection.wab.exception;

import com.til.water_detection.data.Result;
import org.apache.catalina.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public Result<?> handler(Exception e) {
        logger.error("An exception occurred: ", e);
        return Result.error(e.getClass().getName() + ":" + e.getMessage());
    }

}
