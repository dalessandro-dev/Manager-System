package com.dalessandro.ManagerSystem.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String error;

    protected BusinessException(String message, HttpStatus httpStatus, String error) {
        super(message);
        this.httpStatus = httpStatus;
        this.error = error;
    }
}
