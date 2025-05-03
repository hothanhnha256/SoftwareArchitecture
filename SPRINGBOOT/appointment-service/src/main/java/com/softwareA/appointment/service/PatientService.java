package com.softwareA.appointment.service;

import com.softwareA.appointment.client.PatientClient;
import com.softwareA.appointment.model.auth.AuthInfo;
import com.softwareA.appointment.model.patient.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService implements IPatientService {
    private final PatientClient patientClient;

    public Patient getPatient(AuthInfo authInfo) {
        Patient patientApiResponse = patientClient.getPatientInfo(authInfo.getUserId(), authInfo.getUserRole()).getResult();
        return patientApiResponse;
    }
}
