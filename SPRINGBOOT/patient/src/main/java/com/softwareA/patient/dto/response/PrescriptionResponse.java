package com.softwareA.patient.dto.response;

import com.softwareA.patient.dto.Staff;
import com.softwareA.patient.model.patient.Patient;
import com.softwareA.patient.model.prescription.PrescriptionStatus;
import com.softwareA.patient.model.prescription.Prescription_Medication;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PrescriptionResponse {
    String id;
    String medicalOrderId;  // ID of the medical order associated with this prescription
    String doctorId;  // ID of the medical order associated with this prescription
    String pharmacistId;
    PrescriptionStatus status;
    LocalDateTime createdAt;
    List<Prescription_Medication> medications;
    Staff doctor;
    PatientGeneralDTO patient;
}
