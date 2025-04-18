package com.softwareA.patient.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Staff {
    @NotNull
    String id;
    @NotNull
    String name;
    @NotNull
    String role;
    @NotNull
    String phoneNumber;
    @NotNull
    String departmentId;
    @NotNull
    String jobTitle;
    String email;
}