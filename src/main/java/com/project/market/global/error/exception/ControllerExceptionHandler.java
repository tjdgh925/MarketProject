package com.project.market.global.error.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception exception) {
        log.error("error: ", exception);

        ErrorCode errorCode = ErrorCode.getErrorCodeByErrorMessage(exception.getMessage());

        if (errorCode == null) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(errorCode.getMessage(), HttpStatus.valueOf(errorCode.getStatus()));
    }
}
