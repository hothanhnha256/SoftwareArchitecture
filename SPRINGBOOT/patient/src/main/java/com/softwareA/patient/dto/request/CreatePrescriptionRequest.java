package com.softwareA.patient.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreatePrescriptionRequest {
    String pharmacistId;
    @NotNull(message = "medicalOrderId cannot be null")
    String medicalOrderId;
    @NotNull(message = "medications list cannot be null")
    @Size(min = 1, message = "medications list must contain at least one item")
    @Valid
    List<MedicationRequest> medications;
}
