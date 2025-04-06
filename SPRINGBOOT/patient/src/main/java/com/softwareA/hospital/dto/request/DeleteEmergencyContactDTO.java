package com.softwareA.hospital.dto.request;

import jakarta.validation.constraints.NotNull;

public class DeleteEmergencyContactDTO {
    @NotNull(message = "id is required")
    String id;
}
