package com.devteria.identity.mapper;

import com.devteria.identity.dto.request.CreatePatientAccount;
import com.devteria.identity.dto.request.CreatePatientDTO;
import com.devteria.identity.dto.request.UserCreationRequest;
import com.devteria.identity.dto.request.UserUpdateRequest;
import com.devteria.identity.dto.response.UserResponse;
import com.devteria.identity.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    CreatePatientDTO toCreatePatientDTO(CreatePatientAccount request);
    UserCreationRequest toUserCreationRequest(CreatePatientAccount request);
}
