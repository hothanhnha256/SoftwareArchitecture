package com.softwareA.patient.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

// đơn thuốc
@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Auto-generate UUID
    UUID id;
    String patientId;
    String doctorId;
    String phamarcistId;

    // TIME INSERT
    @CreationTimestamp
    @Column(nullable = false, updatable = false)  // Auto-set timestamp, cannot be updated
    private LocalDateTime createdAt;
}
