package com.softwareA.patient.mapper;

import com.softwareA.patient.dto.response.PatientGeneralDTO;
import com.softwareA.patient.model.patient.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientGeneralDTO toGeneralDTO(Patient patient);
}
