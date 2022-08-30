package com.project.market.global.error.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.Collection;
import java.util.List;

@Slf4j
public class InvalidParameterException extends IllegalArgumentException{

    public InvalidParameterException(String message) {
        super(message);
    }

    public static void throwErrorMessage(Errors errors) {
        StringBuilder sb = new StringBuilder();
        List<ObjectError> allErrors = errors.getAllErrors();

        for (ObjectError allError : allErrors) {
            sb.append(allError.getDefaultMessage() + "\n");
        }

        throw new InvalidParameterException(sb.toString());
    }
}
