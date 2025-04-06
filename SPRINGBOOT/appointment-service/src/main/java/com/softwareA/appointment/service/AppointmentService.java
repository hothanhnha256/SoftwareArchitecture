package com.softwareA.appointment.service;

import com.softwareA.appointment.client.PatientClient;
import com.softwareA.appointment.client.StaffClient;
import com.softwareA.appointment.dto.request.CreateAppointmentDTO;
import com.softwareA.appointment.dto.request.GetAppointmentsDTO;
import com.softwareA.appointment.dto.request.GetAvailableDoctorsDTO;
import com.softwareA.appointment.dto.request.UpdateAppointmentDTO;
import com.softwareA.appointment.exception.AppException;
import com.softwareA.appointment.exception.ErrorCode;
import com.softwareA.appointment.model.appointment.Appointment;
import com.softwareA.appointment.model.patient.Patient;
import com.softwareA.appointment.model.staff.Doctor;
import com.softwareA.appointment.repository.AppointmentRepository;
import com.softwareA.appointment.specification.AppointmentSpecification;
import com.softwareA.appointment.strategy.AppointmentUpdateStrategy;
import com.softwareA.hospital.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {
    private final PatientClient patientClient;
    private final StaffClient staffClient;
    private final AppointmentRepository appointmentRepository;
    private final List<AppointmentUpdateStrategy> updateStrategies;

    public String testFeign() {
        // Call the Feign client method
        String response = patientClient.test().getResult();
        log.info("testFeign " + response);
        return response;
    }

    public Appointment updateAppointment(UUID userId, String role, UUID appointmentId, UpdateAppointmentDTO dto) {
        if (dto.getBriefDescription() == null && dto.getStatus() == null) {
            throw new AppException(ErrorCode.BAD_REQUEST, "You must provide at least one field to update");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Appointment not found"));

        AppointmentUpdateStrategy strategy = updateStrategies
                .stream()
                .filter(s -> s.supports(role))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.FORBIDDEN, "Unsupported role: " + role));

        Appointment updatedAppointment = strategy.update(appointment, dto, userId);
        return appointmentRepository.save(updatedAppointment);
    }

//    private Appointment updateAppointmentByUser(Appointment appointment, UpdateAppointmentDTO dto, UUID userId) {
//        // Check if the patient is the owner of the appointment
//        if (!appointment.getPatientId().equals(userId)) {
//            throw new AppException(ErrorCode.FORBIDDEN, "You are not allowed to update this appointment");
//        }
//
//        // Update status if provided
//        if (dto.getStatus() != null) {
//            if (appointment.getStatus().canAppointmentBeUpdatedByPatient(dto.getStatus())) {
//                appointment.setStatus(dto.getStatus());
//            } else {
//                throw new AppException(ErrorCode.FORBIDDEN, "You are not allowed to change this appointment status to " + dto.getStatus());
//            }
//        }
//
//        // Update brief description if provided and status is WAITING
//        if (dto.getBriefDescription() != null) {
//            if (appointment.getStatus() == AppointmentStatus.WAITING) {
//                appointment.setBriefDescription(dto.getBriefDescription());
//            } else {
//                throw new AppException(ErrorCode.FORBIDDEN, "You are not allowed to change this appointment");
//            }
//        }
//
//        return appointmentRepository.save(appointment);
//    }
//
//    private Appointment updateAppointmentByDoctor(Appointment appointment, UpdateAppointmentDTO dto, UUID userId) {
//        // Check if the doctor is the owner of the appointment
//        if (!appointment.getDoctorId().equals(userId)) {
//            throw new AppException(ErrorCode.FORBIDDEN, "You are not allowed to update this appointment");
//        }
//
//        // Update status if provided
//        if (dto.getStatus() != null) {
//            if (appointment.getStatus().canAppointmentBeUpdatedByDoctor(dto.getStatus())) {
//                appointment.setStatus(dto.getStatus());
//            } else {
//                throw new AppException(ErrorCode.FORBIDDEN, "You are not allowed to change this appointment status to " + dto.getStatus());
//            }
//        }
//
//        // Doctors are not allowed to update the brief description
//        if (dto.getBriefDescription() != null) {
//            throw new AppException(ErrorCode.FORBIDDEN, "You are not allowed to change this field");
//        }
//
//        return appointmentRepository.save(appointment);
//    }

    public Page<Appointment> getAppointments(UUID userId, String role, GetAppointmentsDTO dto, Pageable pageable) {
        // to check authorization
        UUID patientId = null;
        UUID doctorId = null;
        if (role.equals("USER")) {
            patientId = userId;
        } else if (role.equals("DOCTOR")) {
            doctorId = userId;
        } else {
            throw new AppException(ErrorCode.FORBIDDEN, "Only user and doctor are allowed to get appointments");
        }
        List<UUID> shiftIds = new ArrayList<>();
        //TODO: add shiftIds
        Specification<Appointment> spec = AppointmentSpecification.getAppointmentsWithFilter(patientId, doctorId, shiftIds, dto.getStatus() != null ? dto.getStatus().toString() : null);
        return appointmentRepository.findAll(spec, pageable);
    }

    public Appointment createAppointment(UUID userId, String role, CreateAppointmentDTO dto) {
        // to check authorization
        if (!role.equals("USER")) {
            throw new AppException(ErrorCode.FORBIDDEN, "Only user are allowed to create an appointment");
        }
        // to check if this patient exists
        Patient patientApiResponse = patientClient.getPatientInfo(userId.toString(), role).getResult();
        if (patientApiResponse == null) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Patient not found");
        }

        log.info("Patient info: " + patientApiResponse.toString());

        //TODO: to check if this doctor exists
        //TODO: to check if this shift exists


        Appointment appointment = Appointment.builder()
                .id(UUID.randomUUID())
                .patientId(userId)
                .doctorId(UUID.randomUUID()) //TODO: temp
                .shiftId(UUID.randomUUID()) //TODO: temp
                .briefDescription(dto.getBriefDescription())
                .build();

        // Save the appointment to the database (this part is not implemented in this example)
        return appointmentRepository.save(appointment);
    }

    public ApiResponse<List<Doctor>> getAvailableDoctors(GetAvailableDoctorsDTO dto, Pageable pageable) {
        ApiResponse<List<Doctor>> doctorResponse = this.staffClient.getAvailableDoctors(dto, pageable);
        //TODO: check how many appointments each doctor has and filter again
        return doctorResponse;
    }
}
