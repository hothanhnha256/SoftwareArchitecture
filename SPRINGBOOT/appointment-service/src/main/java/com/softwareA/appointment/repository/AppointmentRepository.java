package com.softwareA.appointment.repository;

import com.softwareA.appointment.model.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID>, JpaSpecificationExecutor<Appointment> {
    Optional<Appointment> findById(UUID appointmentId);

    List<Appointment> findByPatientIdAndShiftId(UUID patientId, String shiftId);

    List<Appointment> findByDoctorId(String doctorId);

    List<Appointment> findByDoctorIdAndShiftId(String doctorId, String shiftId);
}
