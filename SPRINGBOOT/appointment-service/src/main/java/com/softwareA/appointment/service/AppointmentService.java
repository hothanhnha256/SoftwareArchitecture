package com.softwareA.appointment.service;

import com.softwareA.appointment.client.DepartmentClient;
import com.softwareA.appointment.client.PatientClient;
import com.softwareA.appointment.client.StaffClient;
import com.softwareA.appointment.dto.request.*;
import com.softwareA.appointment.dto.response.AppointmentDetailDTO;
import com.softwareA.appointment.exception.AppException;
import com.softwareA.appointment.exception.ErrorCode;
import com.softwareA.appointment.model.Department;
import com.softwareA.appointment.model.appointment.Appointment;
import com.softwareA.appointment.model.patient.Patient;
import com.softwareA.appointment.model.staff.Doctor;
import com.softwareA.appointment.model.staff.Shift;
import com.softwareA.appointment.repository.AppointmentRepository;
import com.softwareA.appointment.specification.AppointmentSpecification;
import com.softwareA.appointment.strategy.AppointmentUpdateStrategy;
import com.softwareA.patient.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {
    private final PatientClient patientClient;
    private final StaffClient staffClient;
    private final DepartmentClient departmentClient;
    private final AppointmentRepository appointmentRepository;
    private final List<AppointmentUpdateStrategy> updateStrategies;
    private static final int MAX_APPOINTMENTS_PER_SHIFT = 2;

    public String testFeign() {
        // Call the Feign client method
        String response = patientClient.test().getResult();
        log.info("testFeign " + response);
        return response;
    }

    private boolean _checkIfAppointmentIsValid(String doctorId, String shiftId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndShiftId(doctorId, shiftId);
        if (appointments.size() >= MAX_APPOINTMENTS_PER_SHIFT) {
            return false;
        }
        return true;
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
        List<String> shiftIds = new ArrayList<>();
        try {
            ApiResponse<List<Shift>> shiftResponse = staffClient.getShiftOverAPeriod(dto.getStartDate(), dto.getEndDate());
            log.info(shiftResponse.getResult().size() + " shifts found");
            staffClient.getShiftOverAPeriod(dto.getStartDate(), dto.getEndDate()).getResult()
                    .forEach(shift -> {
                        log.info("Shift info: " + shift.getId());
                        shiftIds.add(shift.getId());
                    });
        } catch (Exception e) {
            log.error("Shift not found");
        }
        //TODO: add shiftIds
        GetAppointmentFilter filter = GetAppointmentFilter.builder()
                .patientIds(patientId != null ? List.of(patientId) : null)
                .doctorIds(doctorId != null ? List.of(doctorId) : null)
                .shiftIds(shiftIds)
                .status(dto.getStatus() != null ? dto.getStatus().toString() : null)
                .build();

        Specification<Appointment> spec = AppointmentSpecification.getAppointmentsWithFilter(filter);

        Page<Appointment> appointmentPage = appointmentRepository.findAll(spec, pageable);

        if (!dto.getIncludeDetail()) {
            return appointmentPage;
        }

        List<Appointment> detailedList = appointmentPage.getContent().stream()
                .map(appointment -> {
                    Doctor doctor = null;
                    Shift shift = null;
                    try {
                        doctor = staffClient.getDoctorById(appointment.getDoctorId()).getResult();
                        shift = staffClient.getShiftById(appointment.getShiftId()).getResult();
                    } catch (Exception e) {
                        log.error("Doctor or shift not found");
                    }

                    appointment.setDoctor(doctor);
                    appointment.setShift(shift);
                    return appointment;
                })
                .collect(Collectors.toList());

        Page<Appointment> result = new PageImpl<>(
                detailedList,
                pageable,
                appointmentPage.getTotalElements()
        );
        return result;
    }

    public Appointment createAppointment(UUID userId, String role, CreateAppointmentDTO dto) {
        // to check authorization
        if (!role.equals("USER")) {
            throw new AppException(ErrorCode.FORBIDDEN, "Only user are allowed to create an appointment");
        }
        // to check if this patient exists
        log.info("UserId" + userId.toString());
        Patient patientApiResponse = patientClient.getPatientInfo(userId.toString(), role).getResult();
        if (patientApiResponse == null) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Patient not found");
        }

        log.info("Patient info: " + patientApiResponse.toString());
        try {
            ApiResponse<Doctor> doctorResponse = staffClient.getDoctorById(dto.getDoctorId());
            log.info("Doctor info: " + doctorResponse.getResult().toString());
        } catch (Exception e) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Doctor not found");
        }

        try {
            ApiResponse<Shift> shiftResponse = staffClient.getShiftById(dto.getShiftId());
            log.info("Shift info: " + shiftResponse.getResult().toString());
        } catch (Exception e) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Shift not found");
        }

        if (_checkIfAppointmentIsValid(dto.getDoctorId(), dto.getShiftId())) {
            Appointment appointment = Appointment.builder()
                    .id(UUID.randomUUID())
                    .patientId(userId)
                    .doctorId(dto.getDoctorId())
                    .shiftId(dto.getShiftId())
                    .briefDescription(dto.getBriefDescription())
                    .build();
            // Save the appointment to the database (this part is not implemented in this example)
            return appointmentRepository.save(appointment);
        } else {
            throw new AppException(ErrorCode.FORBIDDEN, "This doctor has too many appointments in this shift");
        }

    }

    public List<Doctor> getAvailableDoctors(GetAvailableDoctorsDTO dto, Pageable pageable) {
        ApiResponse<List<Doctor>> doctorResponse = null;
        String shiftId;
        try {
            doctorResponse = this.staffClient.getAvailableDoctors(dto.getDepartmentId(), "DOCTOR");
        } catch (Exception e) {
            log.info("No doctor in this department");
            return new ArrayList<>();
        }

        try {
            ApiResponse<Shift> shiftResponse = this.staffClient.getShiftByDateTime(dto.getDate().toString(), dto.getHour());
            Shift shift = Optional.ofNullable(shiftResponse)
                    .map(ApiResponse::getResult)
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Shift not found"));

            shiftId = Optional.ofNullable(shift.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Shift ID is missing"));
        } catch (Exception e) {
            log.info("Shift not found");
            return new ArrayList<>();
        }
        List<Doctor> doctors = doctorResponse.getResult();
        // check if this shift, doctor is available
        if (doctors == null || doctors.isEmpty()) {
            return new ArrayList<>();
        }
        doctors = doctors.stream().filter(doctor ->
                _checkIfAppointmentIsValid(doctor.getId(), shiftId)
        ).toList();
        return doctors;
    }

    public ApiResponse<List<Department>> getDepartments() {
        ApiResponse<List<Department>> departmentResponse = this.departmentClient.getDepartments();
        return departmentResponse;
    }
}
