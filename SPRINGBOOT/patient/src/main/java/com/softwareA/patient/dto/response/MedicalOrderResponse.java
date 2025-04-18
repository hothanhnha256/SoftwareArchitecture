package com.softwareA.patient.dto.response;

import com.softwareA.patient.dto.Staff;
import com.softwareA.patient.model.MedicalOrder_OrderItem;
import com.softwareA.patient.model.Patient;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MedicalOrderResponse {
    @NotNull
    String id;
    PatientGeneralDTO patient;
    Staff doctor;
    LocalDateTime createdAt;
    List<MedicalOrder_OrderItem> medicalOrderItems;
}
