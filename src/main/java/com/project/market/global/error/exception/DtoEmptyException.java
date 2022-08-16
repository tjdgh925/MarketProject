package com.project.market.global.error.exception;

public class DtoEmptyException extends RuntimeException{

    public DtoEmptyException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
