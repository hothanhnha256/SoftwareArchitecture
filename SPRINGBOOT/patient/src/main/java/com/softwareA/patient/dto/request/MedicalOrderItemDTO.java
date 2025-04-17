package com.softwareA.patient.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MedicalOrderItemDTO {
    @NotNull
    String medicalOrderId;
    @NotNull
    @Positive
    Integer quantity;

}
