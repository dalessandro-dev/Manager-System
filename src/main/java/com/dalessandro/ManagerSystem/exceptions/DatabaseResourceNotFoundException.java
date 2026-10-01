package com.dalessandro.ManagerSystem.exceptions;

import com.dalessandro.ManagerSystem.helpers.StringHelper;
import org.springframework.http.HttpStatus;

public class DatabaseResourceNotFoundException extends BusinessException {
    public DatabaseResourceNotFoundException(String resourceName, String resourceValue) {
        super(
                formatMessage(resourceName, resourceValue),
                HttpStatus.NOT_FOUND,
                "Resource not found"
        );
    }

    private static String formatMessage(String name, String value) {
        String safeName = (name == null || name.isBlank()) ? "Resource" : StringHelper.capitalize(name.trim());
        String safeValue = (value == null) ? "" : value.trim();

        return safeName + " with the value '" + safeValue + "' was not found";
    }
}
