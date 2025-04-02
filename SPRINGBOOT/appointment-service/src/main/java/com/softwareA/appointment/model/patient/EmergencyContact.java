package com.softwareA.appointment.model.patient;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmergencyContact {
    String id;
    String phoneNumber;
    String address;
    String firstName;
    String lastName;
    EmergencyContactRelationship relationship;
    UUID patientId;
}
