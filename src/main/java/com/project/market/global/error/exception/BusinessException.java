package com.project.market.global.error.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public BusinessException(String errorMessage) {
        super(errorMessage);
    }
}
