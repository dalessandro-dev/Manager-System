package com.dalessandro.ManagerSystem.services;

import com.dalessandro.ManagerSystem.dtos.*;
import com.dalessandro.ManagerSystem.exceptions.DatabaseResourceConflictException;
import com.dalessandro.ManagerSystem.exceptions.DatabaseResourceNotFoundException;
import com.dalessandro.ManagerSystem.models.UserModel;
import com.dalessandro.ManagerSystem.models.enums.Gender;
import com.dalessandro.ManagerSystem.models.enums.Role;
import com.dalessandro.ManagerSystem.repositories.UserRepository;
import com.dalessandro.ManagerSystem.specifications.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto createUser(CreateUserDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new DatabaseResourceConflictException("E-mail", dto.email());
        }

        if (userRepository.existsByCpf(dto.cpf())) {
            throw new DatabaseResourceConflictException("Cpf", dto.cpf());
        }

        if (userRepository.existsByPhone(dto.phone())) {
            throw new DatabaseResourceConflictException("Phone", dto.phone());
        }

        if (userRepository.existsByUsername(dto.username())) {
            throw new DatabaseResourceConflictException("Username", dto.username());
        }

        LocalDateTime now = LocalDateTime.now();

        UserModel user = new UserModel(
                Role.CLIENT,
                now,
                now,
                dto.password(),
                dto.nationality(),
                Gender.valueOf(dto.gender()),
                dto.email(),
                LocalDate.parse(dto.birthDate()),
                dto.phone(),
                dto.cpf(),
                dto.username(),
                dto.name()
        );

        userRepository.save(user);

        return UserResponseDto.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new DatabaseResourceNotFoundException("Id", String.valueOf(id)));

        return UserResponseDto.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<UserResponseDto> getUsers(Pageable pageable, UserFilterDto filter) {
        Specification<UserModel> spec = UserSpecification.withFilters(filter);

        Page<UserResponseDto> users = userRepository.findAll(spec, pageable)
                .map(UserResponseDto::fromEntity);

        return PageResponseDto.fromPage(users);
    }

    @Transactional
    public void deleteUser(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new DatabaseResourceNotFoundException("User", String.valueOf(id)));

        userRepository.delete(user);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UpdateUserDto dto) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new DatabaseResourceNotFoundException("User", String.valueOf(id)));

        if (dto.name() != null) {
            user.setName(dto.name());
        }

        if (dto.username() != null && !dto.username().equals(user.getUsername())) {
            if (userRepository.existsByUsername(dto.username())) {
                throw new DatabaseResourceConflictException("Username", dto.username());
            }

            user.setUsername(dto.username());
        }

        if (dto.password() != null) {
            user.setPassword(dto.password());
        }

        if (dto.birthDate() != null) {
            user.setBirthDate(LocalDate.parse(dto.birthDate()));
        }

        if (dto.nationality() != null) {
            user.setNationality(dto.nationality());
        }

        if (dto.gender() != null) {
            user.setGender(Gender.valueOf(dto.gender()));
        }

        if (dto.phone() != null && !user.getPhone().equals(dto.phone())) {
            if (userRepository.existsByPhone(dto.phone())) {
                throw new DatabaseResourceConflictException("Phone", dto.phone());
            }

            user.setPhone(dto.phone());
        }

        user.setUpdatedAt(LocalDateTime.now());

        return UserResponseDto.fromEntity(user);
    }
}
