package com.dalessandro.ManagerSystem.validation.validators;

import com.dalessandro.ManagerSystem.validation.annotations.ValidPastDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ValidPastDateValidator implements ConstraintValidator<ValidPastDate, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        try {
            LocalDate parsed = LocalDate.parse(value);

            return parsed.isBefore(LocalDate.now());
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
