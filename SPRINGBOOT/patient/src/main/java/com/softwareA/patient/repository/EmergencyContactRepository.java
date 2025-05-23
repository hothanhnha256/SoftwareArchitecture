package com.softwareA.patient.repository;

import com.softwareA.patient.model.patient.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, String> {
    List<EmergencyContact> findByPatientId(UUID patientId);
    Optional<EmergencyContact> findById(String id);
}
