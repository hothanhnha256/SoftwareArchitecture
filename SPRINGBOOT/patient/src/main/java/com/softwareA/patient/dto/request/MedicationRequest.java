package com.softwareA.patient.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class    MedicationRequest {
    @NotNull(message = "medicalOrderItemId cannot be null")
    String id;
    @NotNull(message = "quantity cannot be null")
    @Positive(message = "quantity must be positive")
    Integer quantity;
}
