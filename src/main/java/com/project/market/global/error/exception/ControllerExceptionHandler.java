package com.project.market.global.error.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception exception) {
        log.error("error: ", exception);

        int status = 0;

        if (exception instanceof BusinessException) {
            status = HttpStatus.BAD_REQUEST.value();
        } else if (exception instanceof StockException) {
            status = ErrorCode.NOT_ENOUGH_STOCK.getStatus();
        } else if (exception instanceof EntityNotFoundException) {
            status = HttpStatus.BAD_REQUEST.value();
        } else if (exception instanceof DtoEmptyException) {
            status = HttpStatus.CHECKPOINT.value();
        }

        return new ResponseEntity(exception.getMessage(), HttpStatus.valueOf(status));
    }
}
