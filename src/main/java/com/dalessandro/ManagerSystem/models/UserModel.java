package com.dalessandro.ManagerSystem.models;

import com.dalessandro.ManagerSystem.models.enums.Gender;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserModel implements Serializable {
    public UserModel(
            String name,
            String username,
            String cpf,
            String phone,
            LocalDate birthDate,
            String email,
            char gender,
            String nacionality,
            String password,
            LocalDate createdAt,
            LocalDate updatedAt
    ) {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(name = "birth_date", unique = true, nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, check = @CheckConstraint(constraint = "gender IN ('M', 'F')", name = "chk_user_gender"))
    private Gender gender;

    @Column(nullable = false)
    private String nacionality;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "created_at")
    private LocalDate createdAt;

    @Column(nullable = false, name = "updated_at", check = @CheckConstraint(constraint = "updated_at >= created_at", name = "chk_updated_after_created"))
    private LocalDate updatedAt;
}


