package com.softwareA.appointment.strategy;

import com.softwareA.appointment.dto.request.UpdateAppointmentDTO;
import com.softwareA.appointment.exception.AppException;
import com.softwareA.appointment.exception.ErrorCode;
import com.softwareA.appointment.model.appointment.Appointment;
import com.softwareA.appointment.model.appointment.AppointmentStatus;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class UserUpdateAppointmentStrategy implements AppointmentUpdateStrategy {
    @Override
    public boolean supports(String role) {
        return "USER".equals(role);
    }

    @Override
    public Appointment update(Appointment appointment, UpdateAppointmentDTO dto, UUID userId) {
        if (!appointment.getPatientId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "You are not allowed to update this appointment");
        }

        // Update status if provided
        if (dto.getStatus() != null) {
            if (appointment.getStatus().canAppointmentBeUpdatedByPatient(dto.getStatus())) {
                appointment.setStatus(dto.getStatus());
            } else {
                throw new AppException(ErrorCode.FORBIDDEN, "You are not allowed to change this appointment status to " + dto.getStatus());
            }
        }

        // Update brief description if provided and status is WAITING
        if (dto.getBriefDescription() != null) {
            if (appointment.getStatus() == AppointmentStatus.WAITING) {
                appointment.setBriefDescription(dto.getBriefDescription());
            } else {
                throw new AppException(ErrorCode.FORBIDDEN, "You are not allowed to change this appointment");
            }
        }

        return appointment;
    }
}
