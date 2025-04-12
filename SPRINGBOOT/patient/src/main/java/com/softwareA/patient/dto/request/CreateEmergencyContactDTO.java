package com.softwareA.patient.dto.request;

import com.softwareA.patient.model.EmergencyContactRelationship;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateEmergencyContactDTO {
    @NotNull(message = "phoneNumber is required")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Invalid phone number. It should be 10 to 11 digits long.")
    String phoneNumber;

    @NotNull(message = "address is required")
    String address;

    @NotNull(message = "firstName is required")
    String firstName;

    String lastName;

    @NotNull
    EmergencyContactRelationship relationship;

}
