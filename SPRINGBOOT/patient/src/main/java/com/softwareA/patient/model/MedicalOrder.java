package com.softwareA.patient.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class MedicalOrder {
    @Id
    String id;
    @NotNull(message = "Patient ID cannot be null")
    UUID patientId;
    @NotNull(message = "Doctor ID cannot be null")
    String doctorId;
    @CreationTimestamp
    LocalDateTime createdAt;
    @Transient
    List<MedicalOrderItem> medicalOrderItems;
}
