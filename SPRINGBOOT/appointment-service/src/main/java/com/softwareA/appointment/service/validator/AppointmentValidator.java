package com.softwareA.appointment.service.validator;

import com.softwareA.appointment.model.appointment.Appointment;
import com.softwareA.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AppointmentValidator {
    final int MAX_APPOINTMENTS_PER_SHIFT = 2;
    private final AppointmentRepository appointmentRepository;

    public boolean checkIfDoctorIsAvailable(String doctorId, String shiftId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndShiftId(doctorId, shiftId);
        if (appointments.size() >= MAX_APPOINTMENTS_PER_SHIFT) {
            return false;
        }
        return true;
    }

    public boolean canPatientBookAppointment(UUID patientId, String shiftId) {
        List<Appointment> appointments = appointmentRepository.findByPatientIdAndShiftId(patientId, shiftId);
        return appointments.isEmpty();
    }
}
