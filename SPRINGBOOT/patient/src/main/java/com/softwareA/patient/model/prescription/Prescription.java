package com.softwareA.patient.model.prescription;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

// đơn thuốc
@Entity
@Data
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Auto-generate UUID
    String id;
    @NotNull(message = "medicalOrderId cannot be null")
    @Column(nullable = false)
    String medicalOrderId;  // ID of the medical order associated with this prescription

    @NotNull(message = "doctorId cannot be null")
    @Column(nullable = false)
    String doctorId;  // ID of the medical order associated with this prescription

    String pharmacistId;
    @NotNull(message = "status cannot be null")
    PrescriptionStatus status = PrescriptionStatus.PENDING;  // Default status is PENDING
    @Column(nullable = false, updatable = false)  // Auto-set timestamp, cannot be updated
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
