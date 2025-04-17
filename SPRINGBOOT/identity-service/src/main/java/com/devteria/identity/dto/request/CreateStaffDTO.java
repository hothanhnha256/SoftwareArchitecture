package com.devteria.identity.dto.request;

import com.devteria.identity.model.Sex;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CreateStaffDTO {
    @NotNull
    String id;
    @NotNull
    String name;
    @NotNull
    String role;
    @NotNull
    String phoneNumber;
    @NotNull
    String address;
    @NotNull
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
