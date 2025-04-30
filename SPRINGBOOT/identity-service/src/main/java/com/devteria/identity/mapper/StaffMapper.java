package com.devteria.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.devteria.identity.dto.request.CreateStaffAccountDTO;
import com.devteria.identity.dto.request.CreateStaffDTO;
import com.devteria.identity.dto.request.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    @Mapping(source = "dob", target = "dateOfBirth")
    @Mapping(target = "name", expression = "java(concatNames(request.getLastName(), request.getFirstName()))")
    CreateStaffDTO toCreateStaffDTO(CreateStaffAccountDTO request);

    UserCreationRequest toUserCreationRequest(CreateStaffAccountDTO request);

    @Named("concatNames")
    default String concatNames(String lastName, String firstName) {
        return lastName + " " + firstName; // Concatenate firstname and lastname
    }
}
