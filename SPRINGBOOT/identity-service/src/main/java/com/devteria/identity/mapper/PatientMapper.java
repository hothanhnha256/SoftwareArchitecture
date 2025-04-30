package com.devteria.identity.mapper;

import org.mapstruct.Mapper;

import com.devteria.identity.dto.request.CreatePatientAccount;
import com.devteria.identity.dto.request.CreatePatientDTO;
import com.devteria.identity.dto.request.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    CreatePatientDTO toCreatePatientDTO(CreatePatientAccount request);

    UserCreationRequest toUserCreationRequest(CreatePatientAccount request);
}
