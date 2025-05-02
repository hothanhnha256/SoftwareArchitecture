package com.softwareA.patient.repository;

import com.softwareA.patient.model.prescription.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, String> {
    List<Prescription> findByMedicalOrderId(String medicalOrderId);

    List<Prescription> findByPharmacistId(String medicalOrderId);

    List<Prescription> findByDoctorId(String medicalOrderId);

    List<Prescription> findAllByMedicalOrderIdIn(List<String> ids);
}
