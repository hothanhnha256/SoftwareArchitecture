package com.softwareA.patient.service;

import com.softwareA.patient.dto.request.CreatePatientDTO;
import com.softwareA.patient.dto.request.PatientSearchRequest;
import com.softwareA.patient.dto.request.UpdatePatientDTO;
import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.model.patient.Patient;
import com.softwareA.patient.repository.EmergencyContactRepository;
import com.softwareA.patient.repository.PatientRepository;
import com.softwareA.patient.specification.PatientSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {
    private final PatientRepository patientRepository;
    private final EmergencyContactRepository emergencyContactRepository;

    public Patient createPatient(CreatePatientDTO dto) {
        Patient createdPatient = Patient.builder()
                .id(dto.getId())
                .dob(dto.getDob())
                .firstName(dto.getFirstName())
                .address(dto.getAddress())
                .sex(dto.getSex())
                .phoneNumber(dto.getPhoneNumber())
                .photoUrl(dto.getPhotoUrl())
                .bloodType(dto.getBloodType())
                .citizenId(dto.getCitizenId())
                .email(dto.getEmail())
                .healthInsuranceNumber(dto.getHealthInsuranceNumber())
                .lastName(dto.getLastName())
                .build();

        log.info("Creating patient with ID: {}", dto.getId());
        return patientRepository.save(createdPatient);
    }

    public Optional<Patient> getPatientById(String userId, String role, UUID patientId) {
        if (role.equals("USER"))
        {
            try {
                UUID id = UUID.fromString(userId);
                if (!id.equals(patientId)) {
                    throw new AppException(ErrorCode.FORBIDDEN);
                }
            } catch (IllegalArgumentException e) {
                throw new AppException(ErrorCode.INVALID_UUID);
            }
        }
        else if (!role.equals("ADMIN") && !role.equals("DOCTOR")) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        return patientRepository.findById(patientId);
    }

    public Page<Patient> getAllPatients(String userId, String userRole, PatientSearchRequest request, Pageable pageable) {
        //TODO: add authorization check here
        Specification<Patient> spec = PatientSpecification.withFilters(request);
        return patientRepository.findAll(spec, pageable);
    }

    // NOT TEST YET
    @Transactional
    public Patient updatePatientInfo(String userId, String role, UpdatePatientDTO request) {
        if (!role.equals("USER"))
        {
            throw new AppException(ErrorCode.FORBIDDEN, "Only user can update their own profile");
        }
        UUID patientId;
        try {
            patientId = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_UUID);
        }
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
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
            patient.setBloodType(request.getBloodType()); // TODO: check if this's not error
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
