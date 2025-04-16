package com.softwareA.patient.repository;

import com.softwareA.patient.model.MedicalOrderItem;
import com.softwareA.patient.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, String>,
        JpaSpecificationExecutor<Medication> {
}
