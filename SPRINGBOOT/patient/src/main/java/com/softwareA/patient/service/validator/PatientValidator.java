package com.softwareA.patient.service.validator;

import com.softwareA.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PatientValidator {
    private final PatientRepository patientRepository;

    public boolean patientExists(UUID patientId) {
        return patientRepository.existsById(patientId);
    }
}
