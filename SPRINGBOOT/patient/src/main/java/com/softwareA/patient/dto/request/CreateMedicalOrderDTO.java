package com.softwareA.patient.dto.request;

import com.softwareA.patient.model.MedicalOrderItem;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CreateMedicalOrderDTO {
    @NotNull(message = "Patient ID cannot be null")
    UUID patientId;
    @NotNull(message = "Order Item IDs cannot be null")
    List<MedicalOrderItemDTO> orderItems;
}
