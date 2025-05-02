package com.softwareA.appointment.strategy;

import com.softwareA.appointment.dto.request.GetAppointmentFilter;
import com.softwareA.appointment.dto.request.GetAppointmentsDTO;

import java.util.UUID;

public interface AppointmentRoleFilterStrategy {
    GetAppointmentFilter applyRoleFilter(UUID userId, GetAppointmentsDTO dto);
}
