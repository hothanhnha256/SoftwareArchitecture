package com.softwareA.appointment.filter;

public class AppointmentRoleFilterStrategyFactory {
    private AppointmentRoleFilterStrategyFactory() {
    }

    public static AppointmentRoleFilterStrategy getStrategy(String role) {
        switch (role) {
            case "USER":
                return new PatientRoleStrategy();
            case "DOCTOR":
                return new DoctorRoleStrategy();
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
