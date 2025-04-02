package com.softwareA.appointment.model.appointment;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment {
    @Id
    UUID id;
    @Column(nullable = false)
    UUID patientId;
    @Column(nullable = false)
    UUID doctorId;
    @Column(nullable = false)
    UUID shiftId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Store as a string in DB
    AppointmentStatus status;
    String briefDescription; // Additional notes or comments about the appointment
    @Column(nullable = false)
    LocalDateTime updatedAt;
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    // Constructors, getters, and setters can be generated using Lombok annotations or manually created
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        status = AppointmentStatus.WAITING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
