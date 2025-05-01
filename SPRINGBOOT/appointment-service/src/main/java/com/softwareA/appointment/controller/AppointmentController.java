package com.softwareA.appointment.controller;

import com.softwareA.appointment.dto.request.CreateAppointmentDTO;
import com.softwareA.appointment.dto.request.GetAppointmentsDTO;
import com.softwareA.appointment.dto.request.GetAvailableDoctorsDTO;
import com.softwareA.appointment.dto.request.UpdateAppointmentDTO;
import com.softwareA.appointment.dto.response.AppointmentDetailDTO;
import com.softwareA.appointment.exception.AppException;
import com.softwareA.appointment.exception.ErrorCode;
import com.softwareA.appointment.model.Department;
import com.softwareA.appointment.model.appointment.Appointment;
import com.softwareA.appointment.model.staff.Doctor;
import com.softwareA.appointment.service.AppointmentService;
import com.softwareA.patient.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/test-patient-feign")
    public String testFeign() {
        log.info("testFeign");
        return appointmentService.testFeign();
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<Department>>> getDepartments() {
        log.info("getDepartments");
        ApiResponse<List<Department>> departments = this.appointmentService.getDepartments();
        return ResponseEntity.ok().body(departments);
    }

    @GetMapping("/available-doctors")
    public ResponseEntity<ApiResponse<List<Doctor>>> getAvailableSchedules(@ModelAttribute GetAvailableDoctorsDTO dto,
                                                                           @PageableDefault(size = 15, page = 0) Pageable pageable) {
        log.info("getAvailableDoctors");
        // TÌM 1 LIST DOCTOR dựa trên khoa khám + ngày khám (shift)
        List<Doctor> doctors = this.appointmentService.getAvailableDoctors(dto, pageable);
        return ResponseEntity.ok().body(ApiResponse.<List<Doctor>>builder()
                .result(doctors)
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Appointment>>> getAppointments(@RequestHeader(name = "UserId") String userId,
                                                                          @RequestHeader(name = "UserRole") String role,
                                                                          @ModelAttribute GetAppointmentsDTO dto,
                                                                          @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("getAppointments");
        UUID userUUID;
        try {
            userUUID = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_UUID);
        }

        dto.validateDateRange();

        Page<Appointment> appointmentPage = this.appointmentService.getAppointments(userUUID, role, dto, pageable);

        return ResponseEntity.ok().body(ApiResponse.<List<Appointment>>builder()
                .total(appointmentPage.getTotalPages())
                .page(appointmentPage.getNumber())
                .limit(appointmentPage.getSize())
                .result(appointmentPage.getContent()).build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Appointment>> createAppointment(@RequestHeader(name = "UserId") String userId,
                                                                      @RequestHeader(name = "UserRole") String role,
                                                                      @RequestBody CreateAppointmentDTO dto) {
        log.info("createAppointment");
        UUID patientId;
        try {
            patientId = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_UUID);
        }


        Appointment createdAppointment = appointmentService.createAppointment(patientId, role, dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(ApiResponse.<Appointment>builder().result(createdAppointment).build());

    }

    @PatchMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<Appointment>> updateAppointment(@RequestHeader(name = "UserId") String userId,
                                                                      @RequestHeader(name = "UserRole") String role,
                                                                      @PathVariable("appointmentId") String appointmentId,
                                                                      @RequestBody UpdateAppointmentDTO dto) {
        log.info("updateAppointment");
        UUID userUUID;
        UUID appointmentUUID;
        try {
            userUUID = UUID.fromString(userId);
            appointmentUUID = UUID.fromString(appointmentId);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_UUID);
        }

        Appointment appointment = this.appointmentService.updateAppointment(userUUID, role, appointmentUUID, dto);

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(ApiResponse.<Appointment>builder().result(appointment).build());

    }

}
