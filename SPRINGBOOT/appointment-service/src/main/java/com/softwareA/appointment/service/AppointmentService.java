package com.softwareA.appointment.service;

import com.softwareA.appointment.client.DepartmentClient;
import com.softwareA.appointment.client.PatientClient;
import com.softwareA.appointment.client.StaffClient;
import com.softwareA.appointment.dto.request.*;
import com.softwareA.appointment.exception.AppException;
import com.softwareA.appointment.exception.ErrorCode;
import com.softwareA.appointment.model.auth.AuthInfo;
import com.softwareA.appointment.service.validator.AppointmentValidator;
import com.softwareA.appointment.strategy.AppointmentRoleFilterStrategy;
import com.softwareA.appointment.strategy.AppointmentRoleFilterStrategyFactory;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
    private final AppointmentValidator appointmentValidator;
    private final IShiftService shiftService;
    private final IPatientService patientService;
    private final IStaffService staffService;
    private final AuthorizationService authorizationService;

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


    public Page<Appointment> getAppointments(UUID userId, String role, GetAppointmentsDTO dto, Pageable pageable) {

        AppointmentRoleFilterStrategy filterStrategy = AppointmentRoleFilterStrategyFactory.getStrategy(role);
        GetAppointmentFilter filter = filterStrategy.applyRoleFilter(userId, dto);
        List<String> shiftIds = new ArrayList<>();
        shiftService.getShiftOverAPeriod(dto.getStartDate(), dto.getEndDate())
                .forEach(shift -> {
                    log.info("Shift info: " + shift.getId());
                    shiftIds.add(shift.getId());
                });

//        try {
//            ApiResponse<List<Shift>> shiftResponse = staffClient.getShiftOverAPeriod(dto.getStartDate(), dto.getEndDate());
//            log.info(shiftResponse.getResult().size() + " shifts found");
//            staffClient.getShiftOverAPeriod(dto.getStartDate(), dto.getEndDate()).getResult()
//                    .forEach(shift -> {
//                        log.info("Shift info: " + shift.getId());
//                        shiftIds.add(shift.getId());
//                    });
//        } catch (Exception e) {
//            log.error("Shift not found");
//        }
        filter.setShiftIds(shiftIds);
        filter.setStatus(dto.getStatus() != null ? dto.getStatus().toString() : null);

        Specification<Appointment> spec = AppointmentSpecification.getAppointmentsWithFilter(filter);

        Page<Appointment> appointmentPage = appointmentRepository.findAll(spec, pageable);

        if (!dto.getIncludeDetail()) {
            return appointmentPage;
        }

        List<Appointment> detailedList = this.enrichAppointmentDetail(appointmentPage.getContent());

        Page<Appointment> result = new PageImpl<>(
                detailedList,
                pageable,
                appointmentPage.getTotalElements()
        );
        return result;
    }

    private List<Appointment> enrichAppointmentDetail(List<Appointment> rawAppointments) {
        List<Appointment> detailedList = rawAppointments.stream()
                .map(appointment -> {
                    Doctor doctor = null;
                    Shift shift = null;
                    Patient patient = null;
                    try {
                        doctor = staffService.getDoctorById(appointment.getDoctorId());
                        shift = shiftService.getShiftById(appointment.getShiftId());
                        patient = patientService.getPatient(AuthInfo.builder().userRole("USER").userId(appointment.getPatientId().toString()).build());
                    } catch (Exception e) {
                        log.error("Doctor or shift not found");
                    }

                    appointment.setDoctor(doctor);
                    appointment.setShift(shift);
                    appointment.setPatient(patient);
                    return appointment;
                })
                .toList();
        return detailedList;
    }

    public Appointment createAppointment(UUID userId, String role, CreateAppointmentDTO dto) {
        // check authorization
        authorizationService.authorizeCreateAppointment(role);

        // check if this patient exists
        Patient patient = patientService.getPatient(AuthInfo.builder().userRole(role).userId(userId.toString()).build());
        if (patient == null) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Patient not found");
        }

        // check if the doctor exists
        Doctor doctor = staffService.getDoctorById(dto.getDoctorId());
        if (doctor == null) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Doctor not found");
        }

        // get shift from shiftId and check if it is valid
        Shift shift = shiftService.getShiftById(dto.getShiftId());
        shiftService.checkIfShiftValid(shift);

        if (!appointmentValidator.canPatientBookAppointment(userId, dto.getShiftId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "You have already had an appointment in this shift");
        }

        if (!appointmentValidator.checkIfDoctorIsAvailable(dto.getDoctorId(), dto.getShiftId())) {
            throw new AppException(ErrorCode.FORBIDDEN, "This doctor has too many appointments in this shift");
        }

        Appointment appointment = Appointment.builder()
                .id(UUID.randomUUID())
                .patientId(userId)
                .doctorId(dto.getDoctorId())
                .shiftId(dto.getShiftId())
                .shift(shift)
                .briefDescription(dto.getBriefDescription())
                .build();

        // Save the appointment to the database (this part is not implemented in this example)
        return appointmentRepository.save(appointment);

    }

    public List<Doctor> getAvailableDoctors(GetAvailableDoctorsDTO dto, Pageable pageable) {

        // validate requested schedule datetime
        dto.validateDateRange();

        ApiResponse<List<Doctor>> doctorResponse = null;
        try {
            doctorResponse = this.staffClient.getAvailableDoctors(dto.getDepartmentId(), "DOCTOR");
        } catch (Exception e) {
            log.info("No doctor in this department");
            return new ArrayList<>();
        }

        Shift shift = this.shiftService.getShiftByDateAndTime(dto.getDate().toString(), dto.getHour());
        if (shift == null) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Shift not found");
        }
        List<Doctor> doctors = doctorResponse.getResult();
        // check if this shift, doctor is available
        if (doctors == null || doctors.isEmpty()) {
            return new ArrayList<>();
        }
        doctors = doctors.stream().filter(doctor ->
                appointmentValidator.checkIfDoctorIsAvailable(doctor.getId(), shift.getId())
        ).toList();
        return doctors;
    }

    public ApiResponse<List<Department>> getDepartments() {
        ApiResponse<List<Department>> departmentResponse = this.departmentClient.getDepartments();
        return departmentResponse;
    }
}
