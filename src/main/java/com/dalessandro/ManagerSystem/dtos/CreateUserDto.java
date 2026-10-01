package com.dalessandro.ManagerSystem.dtos;

import com.dalessandro.ManagerSystem.validation.annotations.ValidPastDate;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

public record CreateUserDto(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters long")
        String name,

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters long")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "CPF is required")
        @CPF(message = "CPF must be valid with only numeric digits")
        String cpf,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters long")
        String password,

        @NotBlank(message = "Birth date is required")
        @ValidPastDate()
        String birthDate,

        @NotBlank(message = "Nationality is required")
        @Size(min = 3, max = 50, message = "Nationality must be between 3 and 50 characters long")
        String nationality,

        @NotBlank(message = "Gender is required")
        @Pattern(regexp = "^[MF]$", message = "Gender must be either 'M' or 'F'")
        String gender,

        @NotBlank(message = "Phone is required")
        @Pattern(
                regexp = "^\\d{10,11}$",
                message = "Phone must be valid with only 10 or 11 numeric digits (including DDD)"
        )
        String phone
) {}
