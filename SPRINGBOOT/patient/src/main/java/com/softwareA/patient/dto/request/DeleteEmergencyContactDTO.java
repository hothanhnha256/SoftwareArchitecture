package com.softwareA.patient.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class DeleteEmergencyContactDTO {
    @NotNull(message = "id is required")
    String id;
}
