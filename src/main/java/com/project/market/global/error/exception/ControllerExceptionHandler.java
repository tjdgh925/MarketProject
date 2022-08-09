package com.project.market.global.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessException(BusinessException exception) {

        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
