package com.project.market.global.error.exception;

public class StockException extends RuntimeException {
    public StockException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
