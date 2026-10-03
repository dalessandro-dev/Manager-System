package com.dalessandro.ManagerSystem.models;

import com.dalessandro.ManagerSystem.models.enums.Gender;
import com.dalessandro.ManagerSystem.models.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_users_cpf", columnNames = "cpf"),
                @UniqueConstraint(name = "uk_users_phone", columnNames = "phone"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 50)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 14)
    private String cpf;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 1,
            columnDefinition = "VARCHAR(1)",
            check = @CheckConstraint(name = "chk_users_gender", constraint = "gender IN ('M', 'F')")
    )
    private Gender gender;

    @Column(nullable = false)
    private String nationality;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(
            nullable = false,
            name = "created_at",
            updatable = false
    )
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(
            nullable = false,
            name = "updated_at",
            check = @CheckConstraint(name = "chk_users_updated_after_created", constraint = "updated_at >= created_at")
    )
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role;

    public UserModel(
            Role role,
            LocalDateTime updatedAt,
            LocalDateTime createdAt,
            String password, String nationality,
            Gender gender, String email,
            LocalDate birthDate, String phone,
            String cpf,
            String username,
            String name
    ) {
        this.role = role;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.password = password;
        this.nationality = nationality;
        this.gender = gender;
        this.email = email;
        this.birthDate = birthDate;
        this.phone = phone;
        this.cpf = cpf;
        this.username = username;
        this.name = name;
    }
}