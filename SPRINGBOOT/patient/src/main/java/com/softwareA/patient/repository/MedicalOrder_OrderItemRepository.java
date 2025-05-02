package com.softwareA.patient.repository;

import com.softwareA.patient.model.medical_order.MedicalOrder_OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalOrder_OrderItemRepository extends JpaRepository<MedicalOrder_OrderItem, String> {
    List<MedicalOrder_OrderItem> findByMedicalOrderId(String medicalOrderId);
}
