package com.softwareA.patient.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateMedicalOrderDTO {
    @NotNull(message = "Patient ID cannot be null")
    UUID patientId;
    @NotNull(message = "Order Item IDs cannot be null")
    List<MedicalOrderItemDTO> orderItems;
}
