package com.softwareA.patient.mapper;

import com.softwareA.patient.dto.response.PrescriptionResponse;
import com.softwareA.patient.model.prescription.Prescription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
    // Define mapping methods here if needed
    // For example, if you have a Prescription entity and a PrescriptionDTO, you can define:
     PrescriptionResponse toPrescriptionDetailDTO(Prescription prescription);
    // Prescription toEntity(PrescriptionDTO prescriptionDTO);
}
