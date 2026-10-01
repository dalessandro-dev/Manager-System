package com.dalessandro.ManagerSystem.exceptions;

import com.dalessandro.ManagerSystem.dtos.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorResponseDto response = new ErrorResponseDto(
                httpStatus.value(),
                "Validation Failed",
                "One or more fields are invalid",
                errors,
                request.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ) {
        HttpStatus httpStatus = ex.getHttpStatus();

        ErrorResponseDto response = new ErrorResponseDto(
                httpStatus.value(),
                ex.getError(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatche(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        String message = String.format("Parameter '%s' must be of type '%s'. Received value: '%s'",
                ex.getName(), requiredType, ex.getValue());

        ErrorResponseDto response = new ErrorResponseDto(
                httpStatus.value(),
                "Invalid Parameter Type",
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(response);
    }
}
