package com.softwareA.patient.model.appointment;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment {
    UUID id;
    UUID patientId;
    UUID doctorId;
    UUID shiftId;
    AppointmentStatus status;
    String briefDescription; // Additional notes or comments about the appointment
    LocalDateTime updatedAt;
    LocalDateTime createdAt;
}
