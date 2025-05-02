package com.softwareA.patient.service.validator;

import com.softwareA.patient.repository.MedicalOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MedicalOrderValidator {
    private final MedicalOrderRepository medicalOrderRepository;

    public boolean checkIfMedicalOrderExists(String medicalOrderId) {
        return medicalOrderRepository.existsById(medicalOrderId);
    }

}
