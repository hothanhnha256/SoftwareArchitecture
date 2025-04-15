package com.softwareA.patient.controller;

import com.softwareA.patient.dto.request.CreatePatientDTO;
import com.softwareA.patient.dto.request.PatientSearchRequest;
import com.softwareA.patient.dto.request.UpdatePatientDTO;
import com.softwareA.patient.dto.response.ApiResponse;
import com.softwareA.patient.dto.response.PatientResponse;
import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.model.Patient;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class PatientController {
    private final com.softwareA.patient.service.PatientService patientService;

    // FIND PATIENT BY NAME, CITIZEN ID, HEALTH INSURANCE NUM, database ID,
    // phoneNumber
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Patient>>> getAllPatients(@Nullable PatientSearchRequest request,
                                                                     @RequestHeader("UserId") String UserId,
                                                                     @RequestHeader("UserRole") String Role,
                                                                     @PageableDefault(size = 20, page = 0) Pageable pageable) {
        log.info(pageable.toString());
        log.info(request.toString());
        log.info(UserId);
        log.info(Role);
        Page<Patient> page = patientService.getAllPatients(UserId, Role, request, pageable);
        return ResponseEntity.ok().body(ApiResponse.<List<Patient>>builder()
                .limit(page.getSize())
                .total(page.getTotalPages())
                .page(page.getNumber())
                .result(page.getContent()).build());
    }

    // GET PATIENT BY ID (for doctors, admin)
    @GetMapping(path = "/{patientId}")
    public ResponseEntity<ApiResponse<Patient>> getPatient(@RequestHeader("UserId") String userId,
                                                           @RequestHeader("UserRole") String role,
                                                           @PathVariable("patientId") String patientId) {
        UUID uuid;
        try {
            uuid = UUID.fromString(patientId);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_UUID); // Define an appropriate error code
        }
        Optional<Patient> patient = patientService.getPatientById(userId, role, uuid);
        if (patient.isEmpty()) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND); // Define an appropriate error code
        }
        return ResponseEntity.ok().body(ApiResponse.<Patient>builder().result(patient.get()).build());
    }

    // GET PATIENT PROFILE ID (for patients themselves)
    @GetMapping(path = "/profile")
    public ResponseEntity<ApiResponse<Patient>> getPatientProfile(@RequestHeader("UserId") String userId,
                                                                  @RequestHeader("UserRole") String role) {
        log.info("getPatientProfile");
        UUID uuid;
        try {
            uuid = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_UUID); // Define an appropriate error code
        }
        Optional<Patient> patient = patientService.getPatientById(userId, role, uuid);
        if (patient.isEmpty()) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND); // Define an appropriate error code
        }
        return ResponseEntity.ok().body(ApiResponse.<Patient>builder().result(patient.get()).build());
    }

    // UPDATE PATIENT INFO (for patients themselves)
    @PatchMapping(path = "/")
    public ApiResponse<PatientResponse> updatePatientInfo(@RequestHeader("UserId") String userId,
                                                          @RequestHeader("UserRole") String role,
                                                          @Valid @RequestBody UpdatePatientDTO dto) {
        // get id from jwt token
        log.info("updatePatientInfo");
        Patient updatedPatient = this.patientService.updatePatientInfo(userId, role, dto);
        PatientResponse response = PatientResponse.builder().patient(updatedPatient).build();
        return ApiResponse.<PatientResponse>builder().result(response).build();
    }

    // CREATE PATIENT INFO
    @PostMapping(path = "/")
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(@Valid @RequestBody CreatePatientDTO dto) {
        Patient patient = this.patientService.createPatient(dto);
        PatientResponse response = PatientResponse.builder().patient(patient).build();
        return ResponseEntity.ok().body(ApiResponse.<PatientResponse>builder().result(response).build());
    }


}