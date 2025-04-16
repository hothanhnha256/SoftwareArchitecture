package com.softwareA.patient.repository;

import com.softwareA.patient.model.Allergy;
import com.softwareA.patient.model.MedicalOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, String> {
}
