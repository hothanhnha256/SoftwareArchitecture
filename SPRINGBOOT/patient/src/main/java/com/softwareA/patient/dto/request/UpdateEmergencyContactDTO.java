package com.softwareA.patient.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
public class UpdateEmergencyContactDTO {
    @NotNull(message = "id is required")
    String id;
    String phoneNumber;
    String address;
    String firstName;
    String lastName;
    String relationship;
}
