package com.softwareA.appointment.strategy;

import com.softwareA.appointment.dto.request.GetAppointmentFilter;
import com.softwareA.appointment.dto.request.GetAppointmentsDTO;

import java.util.List;
import java.util.UUID;

public class DoctorRoleStrategy implements AppointmentRoleFilterStrategy {
    @Override
    public GetAppointmentFilter applyRoleFilter(UUID userId, GetAppointmentsDTO dto) {
        return GetAppointmentFilter.builder()
                .doctorIds(List.of(String.valueOf(userId)))
                .status(dto.getStatus() != null ? dto.getStatus().name() : null)
                .build();
    }
}
