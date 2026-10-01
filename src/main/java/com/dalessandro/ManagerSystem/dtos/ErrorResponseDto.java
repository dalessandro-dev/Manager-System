package com.dalessandro.ManagerSystem.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDto(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        Map<String, String> fieldErrors,
        String path
) {
    public ErrorResponseDto(Integer status, String error, String message, Map<String, String> fieldErrors, String path) {
        this(Instant.now(), status, error, message, fieldErrors, path);
    }

    public ErrorResponseDto(Integer status, String error, String message, String path) {
        this(Instant.now(), status, error, message, null, path);
    }
}
