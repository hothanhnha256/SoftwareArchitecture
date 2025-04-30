package com.devteria.identity.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

import com.devteria.identity.constant.Roles;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    LocalDate createdAt;
    LocalDate updatedAt;

    @Enumerated(EnumType.STRING)
    Roles role;
}
