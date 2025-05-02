package com.softwareA.patient.repository;

import com.softwareA.patient.model.medical_order.MedicalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalOrderRepository extends JpaRepository<MedicalOrder, String> {
    List<MedicalOrder> findByPatientId(UUID patientId);

    List<MedicalOrder> findByDoctorId(String doctorId);
}
