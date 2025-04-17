package com.softwareA.patient.controller;

import com.softwareA.patient.dto.request.CreateMedicalOrderDTO;
import com.softwareA.patient.dto.response.ApiResponse;
import com.softwareA.patient.model.MedicalOrder;
import com.softwareA.patient.model.auth.AuthInfo;
import com.softwareA.patient.service.MedicalOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medical-orders")
public class MedicalOrderController {
    private static final Logger log = LoggerFactory.getLogger(MedicalOrderController.class);
    private final MedicalOrderService medicalOrderService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<MedicalOrder>> createMedicalOrder(@RequestHeader("UserRole") String userRole,
                                                                        @RequestHeader("UserId") String userId,
                                                                        @Valid @RequestBody CreateMedicalOrderDTO dto) {
        log.info("Creating medical order with data: {}\nUserRole: {}\nUserId: {}", dto, userRole, userId);
        AuthInfo authInfo = AuthInfo.builder()
                .userId(userId)
                .userRole(userRole)
                .build();
        MedicalOrder medicalOrder = this.medicalOrderService.createMedicalOrder(dto, authInfo);
        return ResponseEntity.ok().body(ApiResponse.<MedicalOrder>builder()
                .result(medicalOrder)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicalOrder>> getMedicalOrderById(AuthInfo authInfo,
                                                                         @PathVariable String id) {
        MedicalOrder medicalOrder = this.medicalOrderService.getMedicalOrderById(id, authInfo);
        return ResponseEntity.ok().body(ApiResponse.<MedicalOrder>builder()
                .result(medicalOrder)
                .build());
    }
}
