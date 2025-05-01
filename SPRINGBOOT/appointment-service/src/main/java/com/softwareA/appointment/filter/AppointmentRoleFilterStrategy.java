package com.softwareA.appointment.filter;

import com.softwareA.appointment.dto.request.GetAppointmentFilter;
import com.softwareA.appointment.dto.request.GetAppointmentsDTO;

import java.util.UUID;

public interface AppointmentRoleFilterStrategy {
    GetAppointmentFilter applyRoleFilter(UUID userId, GetAppointmentsDTO dto);
}
