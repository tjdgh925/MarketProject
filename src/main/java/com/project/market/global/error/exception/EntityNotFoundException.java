package com.project.market.global.error.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

}