package com.softwareA.appointment.service;

import com.softwareA.appointment.model.staff.Doctor;

public interface IStaffService {
    Doctor getDoctorById(String staffId);
}
