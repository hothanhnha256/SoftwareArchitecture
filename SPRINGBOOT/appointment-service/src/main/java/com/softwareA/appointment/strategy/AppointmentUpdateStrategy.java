package com.softwareA.appointment.strategy;

import com.softwareA.appointment.dto.request.UpdateAppointmentDTO;
import com.softwareA.appointment.model.appointment.Appointment;

import java.util.UUID;

public interface AppointmentUpdateStrategy {
    boolean supports(String role);
    Appointment update(Appointment appointment, UpdateAppointmentDTO dto, UUID userId);
}
