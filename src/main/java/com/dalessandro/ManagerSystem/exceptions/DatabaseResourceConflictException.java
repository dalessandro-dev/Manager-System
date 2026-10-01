package com.dalessandro.ManagerSystem.exceptions;

import com.dalessandro.ManagerSystem.helpers.StringHelper;
import org.springframework.http.HttpStatus;

public class DatabaseResourceConflictException extends BusinessException {
    public DatabaseResourceConflictException(String resourceName, String resourceValue) {
        super(
                formatMessage(resourceName, resourceValue),
                HttpStatus.CONFLICT,
                "Resource Conflict"
        );
    }

    private static String formatMessage(String name, String value) {
        String safeName = (name == null || name.isBlank()) ? "Resource" : StringHelper.capitalize(name.trim());
        String safeValue = (value == null) ? "" : value.trim();

        return safeName + " already in use: " + safeValue;
    }
}
