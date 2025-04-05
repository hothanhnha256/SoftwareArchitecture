package com.softwareA.patient.controller;

import com.softwareA.patient.dto.request.CreateEmergencyContactDTO;
import com.softwareA.patient.dto.request.CreatePatientDTO;
import com.softwareA.patient.dto.request.PatientSearchRequest;
import com.softwareA.patient.dto.request.UpdateEmergencyContactDTO;
import com.softwareA.patient.dto.request.UpdatePatientDTO;
import com.softwareA.patient.dto.response.ApiResponse;
import com.softwareA.patient.dto.response.PatientResponse;
import com.softwareA.patient.dto.response.PatientsResponse;
import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.model.EmergencyContact;
import com.softwareA.patient.model.Patient;
import com.softwareA.patient.payment.BillingService;
import com.softwareA.patient.service.PatientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class PatientController {
    private final PatientService patientService;

    // FIND PATIENT BY NAME, CITIZEN ID, HEALTH INSURANCE NUM, database ID,
    // phoneNumber
    @GetMapping
    public ResponseEntity<ApiResponse<PatientsResponse>> getAllPatients(PatientSearchRequest request, @RequestHeader("UserId") String UserId, @RequestHeader("UserRole") String Role ) {
        // TODO: add filter condition here
        System.out.print(request.toString());
        System.out.print(UserId);
        log.info(request.toString());
        log.info(UserId);
        log.info(Role);
        List<Patient> patients = patientService.getAllPatients(request);
        PatientsResponse response = PatientsResponse.builder().limit(10).page(1).total(10).patients(patients).build();
        return ResponseEntity.ok().body(ApiResponse.<PatientsResponse>builder().result(response).build());
    }

    // GET PATIENT BY ID (for patients themselves)
    @GetMapping(path = "/{patientId}")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatient(@PathVariable("patientId") String patientId) {
        UUID uuid;
        try {
            uuid = UUID.fromString(patientId);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_UUID); // Define an appropriate error code
        }
        Optional<Patient> patient = patientService.getPatientById(uuid);
        if (patient.isEmpty()) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND); // Define an appropriate error code
        }
        PatientResponse response = PatientResponse.builder().patient(patient.get()).build();
        return ResponseEntity.ok().body(ApiResponse.<PatientResponse>builder().result(response).build());
    }

    // UPDATE PATIENT INFO (for patients themselves)
    @PatchMapping(path = "/")
    public ApiResponse<PatientResponse> updatePatientInfo(@Valid @RequestBody UpdatePatientDTO dto) {
        // get id from jwt token
        String userId = "temp";
        Patient updatedPatient = this.patientService.updatePatientInfo(userId, dto);
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

    // CREATE EMERGENCY CONTACT (for patients themselves)
    @PostMapping(path = "/emergency-contact")
    public ResponseEntity<ApiResponse<EmergencyContact>> addEmergencyContact(
            @RequestBody CreateEmergencyContactDTO dto) {
        // get id from jwt token
        String userId = "temp";
        EmergencyContact createdContact = this.patientService.createEmergencyContact(userId, dto);

        return ResponseEntity.status(201)
                .body(ApiResponse.<EmergencyContact>builder().result(createdContact).build());
    }

    @DeleteMapping(path = "/emergency-contact/{contactId}")
    public ResponseEntity<ApiResponse<Void>> deleteEmergencyContact(@PathVariable("contactId") String contactId) {
        // get id from jwt token
        String userId = "temp";
        this.patientService.deleteEmergencyContact(userId, contactId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/emergency-contact/{contactId}")
    public ResponseEntity<ApiResponse<EmergencyContact>> updateEmergencyContact(
            @PathVariable("contactId") String contactId, @RequestBody UpdateEmergencyContactDTO dto) {
        // get id from jwt token
        String userId = "temp";
        EmergencyContact updatedEmergencyContact = this.patientService.updateEmergencyContact(userId, contactId, dto);
        return ResponseEntity.ok()
                .body(ApiResponse.<EmergencyContact>builder().result(updatedEmergencyContact).build());
    }
}