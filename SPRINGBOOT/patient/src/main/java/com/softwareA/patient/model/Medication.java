package com.softwareA.patient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Medication {
    String code;
    @NotNull(message = "name is required")
    String name;
    @NotNull(message = "manufacturer is required")
    String manufacturer;
    @NotNull
    String dosageForm; // e.g., Tablet, Syrup, Injection
    @NotNull
    String strength; // e.g., 500mg, 250mg/5mL
    String description;
    @NotNull
    Integer quantity;
    @NotNull
    Double price;
    LocalDateTime createdAt;
}
