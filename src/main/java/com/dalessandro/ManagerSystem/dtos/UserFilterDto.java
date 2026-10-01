package com.dalessandro.ManagerSystem.dtos;

public record UserFilterDto(
    String name,
    String email,
    String phone,
    String nationality,
    String birthDate,
    String gender
) {}
