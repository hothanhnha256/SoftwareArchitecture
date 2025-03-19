package com.softwareA.patient.service;

import com.softwareA.patient.model.Patient;
import com.softwareA.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public Optional<Patient> getPatientById(UUID patientId) {
        return patientRepository.findById(patientId);
    }

    public List<Patient> getAllPatients() {
        //TODO: add search here condition
        return patientRepository.findAll();
    }
}
