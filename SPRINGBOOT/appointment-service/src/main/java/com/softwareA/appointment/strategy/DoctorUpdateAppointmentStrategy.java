package com.softwareA.appointment.strategy;

import com.softwareA.appointment.dto.request.UpdateAppointmentDTO;
import com.softwareA.appointment.exception.AppException;
import com.softwareA.appointment.exception.ErrorCode;
import com.softwareA.appointment.model.appointment.Appointment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DoctorUpdateAppointmentStrategy implements AppointmentUpdateStrategy {
    @Override
    public boolean supports(String role) {
        return "DOCTOR".equals(role);
    }

    @Override
    public Appointment update(Appointment appointment, UpdateAppointmentDTO dto, UUID userId) {
        if (!appointment.getDoctorId().equals(userId.toString())) {
            throw new AppException(ErrorCode.FORBIDDEN, "You are not allowed to update this appointment");
        }

        // Update status if provided
        if (dto.getStatus() != null) {
            if (appointment.getStatus().canAppointmentBeUpdatedByDoctor(dto.getStatus())) {
                appointment.setStatus(dto.getStatus());
            } else {
                throw new AppException(ErrorCode.FORBIDDEN, "You are not allowed to change this appointment status to " + dto.getStatus());
            }
        }

        // Doctors are not allowed to update the brief description
        if (dto.getBriefDescription() != null) {
            throw new AppException(ErrorCode.FORBIDDEN, "You are not allowed to change this field");
        }

        return appointment;
    }
}
