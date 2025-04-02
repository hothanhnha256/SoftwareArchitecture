package com.softwareA.appointment.model.appointment;

import java.util.List;
import java.util.Map;

public enum AppointmentStatus {
    WAITING,
    NO_SHOW,
    CONFIRMED,
    REJECTED,
    COMPLETED,
    CANCELED,
    EXPIRED;
    private static final Map<AppointmentStatus, List<AppointmentStatus>> DOCTOR_ALLOWED_TRANSITIONS = Map.of(
            WAITING, List.of(CONFIRMED, REJECTED),
            CONFIRMED, List.of(COMPLETED, NO_SHOW)
    );

    public boolean canAppointmentBeUpdatedByDoctor(AppointmentStatus status) {
        return DOCTOR_ALLOWED_TRANSITIONS.get(this) != null && DOCTOR_ALLOWED_TRANSITIONS.get(this).contains(status);
    }


    private static final Map<AppointmentStatus, List<AppointmentStatus>> PATIENT_ALLOWED_TRANSITIONS = Map.of(
            WAITING, List.of(CANCELED),
            CONFIRMED, List.of(CANCELED)
    );

    public boolean canAppointmentBeUpdatedByPatient(AppointmentStatus status) {
        return DOCTOR_ALLOWED_TRANSITIONS.get(this) != null && PATIENT_ALLOWED_TRANSITIONS.get(this).contains(status);
    }
}