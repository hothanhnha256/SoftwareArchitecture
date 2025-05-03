package com.softwareA.appointment.service;

import com.softwareA.appointment.model.auth.AuthInfo;
import com.softwareA.appointment.model.patient.Patient;

public interface IPatientService {
    Patient getPatient(AuthInfo authInfo);
}
