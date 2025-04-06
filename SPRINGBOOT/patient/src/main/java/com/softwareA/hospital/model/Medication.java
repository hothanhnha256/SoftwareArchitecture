package com.softwareA.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// THUỐC MEN
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @NotNull(message = "name is required")
    @Column(nullable = false)
    String name;
    @NotNull(message = "manufacturer is required")
    @Column(nullable = false)
    private String manufacturer;
    @NotNull
    @Column(nullable = false)
    private String dosageForm; // e.g., Tablet, Syrup, Injection
    @NotNull
    private String strength; // e.g., 500mg, 250mg/5mL
    private String description;

    // TIME INSERT
    @CreationTimestamp
    @Column(nullable = false, updatable = false)  // Auto-set timestamp, cannot be updated
    private LocalDateTime createdAt;
}
