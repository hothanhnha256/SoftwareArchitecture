package com.softwareA.patient.repository;

import com.softwareA.patient.model.MedicalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalOrderRepository extends JpaRepository<MedicalOrder, String> {

}
