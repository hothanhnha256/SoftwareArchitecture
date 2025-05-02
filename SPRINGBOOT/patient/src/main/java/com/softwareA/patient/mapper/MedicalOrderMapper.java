package com.softwareA.patient.mapper;

import com.softwareA.patient.dto.response.MedicalOrderResponse;
import com.softwareA.patient.model.medical_order.MedicalOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalOrderMapper {
    MedicalOrderResponse toMedicalOrderResponse(MedicalOrder order);
}
