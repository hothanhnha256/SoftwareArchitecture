package com.softwareA.patient.repository;

import com.softwareA.patient.model.MedicalOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalOrderItemRepository extends JpaRepository<MedicalOrderItem, String>,
        JpaSpecificationExecutor<MedicalOrderItem> {
}
