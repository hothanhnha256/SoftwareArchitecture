package com.softwareA.patient.model.prescription;

import com.softwareA.patient.model.Medication;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@IdClass(Prescription_Medication_ID.class)
@Data
public class Prescription_Medication {
    @NotNull(message = "prescriptionId cannot be null")
    @Column(nullable = false)
    @Id
    String prescriptionId;
    @NotNull(message = "medicationCode cannot be null")
    @Column(nullable = false)
    @Id
    String medicationCode;
    @NotNull(message = "quantity cannot be null")
    @Positive(message = "quantity must be positive")
    Integer quantity;
    @Transient
    String name;
    @Transient
    String manufacturer;
    @Transient
    String dosageForm; // e.g., Tablet, Syrup, Injection
    @Transient
    String strength; // e.g., 500mg, 250mg/5mL
    @Transient
    String description;
    @Transient
    Double price;
}
