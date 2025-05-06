package com.softwareA.patient.controller;

import com.softwareA.patient.dto.request.CreatePrescriptionRequest;
import com.softwareA.patient.dto.response.ApiResponse;
import com.softwareA.patient.dto.response.MedicalOrderResponse;
import com.softwareA.patient.dto.response.PrescriptionResponse;
import com.softwareA.patient.model.auth.AuthInfo;
import com.softwareA.patient.model.prescription.Prescription;
import com.softwareA.patient.service.PrescriptionService;
import com.softwareA.patient.utils.PrescriptionPDFPrinter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
@Slf4j
public class PrescriptionController {
    private final PrescriptionService prescriptionService;
    private final PrescriptionPDFPrinter prescriptionPDFPrinter;

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

    @GetMapping("/{id}/pdf")
    public ResponseEntity<ApiResponse<byte[]>> getPrescriptionPdf(@RequestHeader("UserRole") String userRole,
                                                                  @RequestHeader("UserId") String userId,
                                                                  @PathVariable String id) {
        log.info("getPrescriptionPdf with id: {}\nUserRole: {}\nUserId: {}", id, userRole, userId);
        AuthInfo authInfo = AuthInfo.builder()
                .userId(userId)
                .userRole(userRole)
                .build();
        PrescriptionResponse prescription = this.prescriptionService.getPrescriptionDetail(id, authInfo);
        // convert to pdf byte stream
        byte[] pdfResult = prescriptionPDFPrinter.print(prescription);
        return ResponseEntity.ok().body(ApiResponse.<byte[]>builder()
                .result(pdfResult)
                .build());
    }

}
