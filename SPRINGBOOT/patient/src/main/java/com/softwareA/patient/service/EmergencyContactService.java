package com.softwareA.patient.service;

import com.softwareA.patient.dto.request.CreateEmergencyContactDTO;
import com.softwareA.patient.dto.request.UpdateEmergencyContactDTO;
import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.model.patient.EmergencyContact;
import com.softwareA.patient.model.patient.Patient;
import com.softwareA.patient.repository.EmergencyContactRepository;
import com.softwareA.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmergencyContactService {
    private final EmergencyContactRepository emergencyContactRepository;
    private final PatientRepository patientRepository;

    public List<EmergencyContact> getEmergencyContacts(UUID patientId) {
        return emergencyContactRepository.findByPatientId(patientId);
    }

    public EmergencyContact createEmergencyContact(String userId, String role, CreateEmergencyContactDTO dto) {
        if (!role.equals("USER"))
            throw new AppException(ErrorCode.FORBIDDEN, "Only patients can create emergency contacts");
        UUID patientId;
        try {
            patientId = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_UUID);
        }
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isEmpty())
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        EmergencyContact createdEmergencyContact = EmergencyContact.builder()
                .address(dto.getAddress())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .patientId(patientId)
                .phoneNumber(dto.getPhoneNumber())
                .relationship(dto.getRelationship())
                .build();
        return emergencyContactRepository.save(createdEmergencyContact);
    }

    public void deleteEmergencyContact(String userId, String role, String contactId) {
        if (!role.equals("USER"))
            throw new AppException(ErrorCode.FORBIDDEN, "Only patients can delete emergency contacts");
        UUID patientId;
        try {
            patientId = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_UUID);
        }
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isEmpty())
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        Optional<EmergencyContact> contact = emergencyContactRepository.findById(contactId);
        if (contact.isEmpty())
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        emergencyContactRepository.delete(contact.get());
    }

    public EmergencyContact updateEmergencyContact(String userId, String role, String contactId, UpdateEmergencyContactDTO dto) {
        if (!role.equals("USER"))
            throw new AppException(ErrorCode.FORBIDDEN, "Only patients can update emergency contacts");
        UUID patientId;
        try {
            patientId = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_UUID);
        }
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isEmpty())
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        Optional<EmergencyContact> contact = emergencyContactRepository.findById(contactId);
        if (contact.isEmpty())
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);


        EmergencyContact existingContact = contact.get();
        if (!existingContact.getPatientId().equals(patientId))
        {
            throw new AppException(ErrorCode.FORBIDDEN, "You are not allowed to update this emergency contact");
        }
        if (dto.getAddress() != null) {
            existingContact.setAddress(dto.getAddress());
        }
        if (dto.getFirstName() != null) {
            existingContact.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            existingContact.setLastName(dto.getLastName());
        }
        if (dto.getPhoneNumber() != null) {
            existingContact.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getRelationship() != null) {
            existingContact.setRelationship(dto.getRelationship());
        }
        return emergencyContactRepository.save(existingContact);
    }

}
