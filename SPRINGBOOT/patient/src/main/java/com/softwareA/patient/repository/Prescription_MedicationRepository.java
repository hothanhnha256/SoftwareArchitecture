package com.softwareA.patient.repository;

import com.softwareA.patient.model.prescription.Prescription;
import com.softwareA.patient.model.prescription.Prescription_Medication;
import com.softwareA.patient.model.prescription.Prescription_Medication_ID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Prescription_MedicationRepository extends JpaRepository<Prescription_Medication, Prescription_Medication_ID> {
    List<Prescription_Medication> findAllByPrescriptionId(String prescriptionId);
}
