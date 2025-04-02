package com.softwareA.patient.model;

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
@Entity
public class EmergencyContact {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @NotNull(message = "phoneNumber is required")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Invalid phone number. It should be 10 to 11 digits long.")
    @Column(nullable = false)
    String phoneNumber;
    @NotNull(message = "address is required")
    @Column(nullable = false)
    String address;
    @NotNull(message = "firstName is required")
    @Column(nullable = false)
    String firstName;
    String lastName;
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    EmergencyContactRelationship relationship;

    @NotNull
    @Column(nullable = false)
    UUID patientId;

}
