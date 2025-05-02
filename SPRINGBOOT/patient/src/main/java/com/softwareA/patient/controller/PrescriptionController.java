package com.softwareA.patient.controller;

import com.softwareA.patient.dto.request.CreatePrescriptionRequest;
import com.softwareA.patient.dto.response.ApiResponse;
import com.softwareA.patient.dto.response.PrescriptionResponse;
import com.softwareA.patient.model.auth.AuthInfo;
import com.softwareA.patient.model.prescription.Prescription;
import com.softwareA.patient.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> createPrescription(@RequestHeader("UserId") String userId,
                                                                                @RequestHeader("UserRole") String role,
                                                                                @Valid @RequestBody CreatePrescriptionRequest request) {
        AuthInfo authInfo = AuthInfo.builder()
                .userId(userId)
                .userRole(role)
                .build();
        PrescriptionResponse prescription = prescriptionService.createPrescription(request, authInfo);
        return ResponseEntity.ok(ApiResponse.<PrescriptionResponse>builder()
                .message("Prescription created successfully")
                .result(prescription)
                .build());
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Prescription>>> getPrescriptions(@RequestHeader("UserId") String userId,
                                                                            @RequestHeader("UserRole") String role) {
        AuthInfo authInfo = AuthInfo.builder()
                .userId(userId)
                .userRole(role)
                .build();
        List<Prescription> prescriptions = prescriptionService.getPrescriptions(authInfo);
        return ResponseEntity.ok(ApiResponse.<List<Prescription>>builder()
                .message("Prescriptions retrieved successfully")
                .result(prescriptions)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> getPrescriptionDetail(@RequestHeader("UserId") String userId,
                                                                                @RequestHeader("UserRole") String role,
                                                                                   @PathVariable String id) {
        AuthInfo authInfo = AuthInfo.builder()
                .userId(userId)
                .userRole(role)
                .build();
        PrescriptionResponse prescription = prescriptionService.getPrescriptionDetail(id, authInfo);
        return ResponseEntity.ok(ApiResponse.<PrescriptionResponse>builder()
                .message("Prescription created successfully")
                .result(prescription)
                .build());
    }

}
