package com.dalessandro.ManagerSystem.dtos;

import com.dalessandro.ManagerSystem.models.UserModel;

public record UserResponseDto(
        String name,
        String username,
        String email,
        String cpf,
        String birthDate,
        String nationality,
        String gender,
        String phone,
        String createdAt,
        String updatedAt
) {
    public static UserResponseDto fromEntity(UserModel user) {
        return new UserResponseDto(
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getCpf(),
                user.getBirthDate().toString(),
                user.getNationality(),
                user.getGender().toString(),
                user.getPhone(),
                user.getCreatedAt().toString(),
                user.getUpdatedAt().toString()
        );
    }
}
