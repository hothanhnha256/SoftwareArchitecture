package com.softwareA.patient.service;

import com.softwareA.patient.dto.request.PatientSearchRequest;
import com.softwareA.patient.dto.request.UpdatePatientDTO;
import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.model.BloodType;
import com.softwareA.patient.model.Patient;
import com.softwareA.patient.repository.PatientRepository;
import com.softwareA.patient.specification.PatientSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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

    public List<Patient> getAllPatients(PatientSearchRequest request) {
        Specification<Patient> spec = PatientSpecification.withFilters(request);
        return patientRepository.findAll(spec);
    }

    // NOT TEST YET
    @Transactional
    public Patient updatePatientInfo(UpdatePatientDTO request) {
        Optional<Patient> patientOptional = patientRepository.findById(request.getId());
        if (patientOptional.isEmpty())
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        Patient patient = patientOptional.get();
        if (request.getFirstName() != null) {
            patient.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            patient.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            patient.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            patient.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getDob() != null) {
            patient.setDob(request.getDob());
        }
        if (request.getBloodType() != null) {
            patient.setBloodType(BloodType.valueOf(request.getBloodType())); //TODO: check if this's not error
        }
        if (request.getAddress() != null) {
            patient.setAddress(request.getAddress());
        }
        if (request.getPhotoUrl() != null) {
            patient.setPhotoUrl(request.getPhotoUrl());
        }


        return patientRepository.save(patient); // JPA automatically updates if entity exists
    }
}
