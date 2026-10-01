package com.dalessandro.ManagerSystem.dtos;

import com.dalessandro.ManagerSystem.validation.annotations.ValidPastDate;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters long")
        String name,

        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters long")
        String username,

        @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters long")
        String password,

        @ValidPastDate
        String birthDate,

        @Size(min = 3, max = 50, message = "Nationality must be between 3 and 50 characters long")
        String nationality,

        @Pattern(regexp = "^[MF]$", message = "Gender must be either 'M' or 'F'")
        String gender,

        @Pattern(
                regexp = "^\\d{10,11}$",
                message = "Phone must be valid with only 10 or 11 numeric digits (including DDD)"
        )
        String phone
) {}
