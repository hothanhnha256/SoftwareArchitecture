package com.example.staff_service.Entity;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Document(collection = "staff")
public class Staff {
    @Id
    String id;
    @NotNull
    String name;
    @NotNull
    String role;
    @NotNull
    String phoneNumber;
    @NotNull
    String address;
    String departmentId;
    @NotNull
    String jobTitle;
    String email;
    @NotNull
    Sex sex;
    @NotNull
    LocalDate dateOfBirth;
    String avatarUrl;
}
