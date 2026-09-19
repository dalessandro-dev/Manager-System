package com.dalessandro.ManagerSystem.models;

import com.dalessandro.ManagerSystem.models.enums.Gender;
import com.dalessandro.ManagerSystem.models.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserModel implements Serializable {
    public UserModel() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(name = "birth_date", nullable = false)
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
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at", check = @CheckConstraint(constraint = "updated_at >= created_at", name = "chk_updated_after_created"))
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}


