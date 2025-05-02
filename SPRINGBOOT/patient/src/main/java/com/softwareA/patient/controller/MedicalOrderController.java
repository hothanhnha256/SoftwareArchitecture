package com.softwareA.patient.controller;

import com.softwareA.patient.dto.request.CreateMedicalOrderDTO;
import com.softwareA.patient.dto.response.ApiResponse;
import com.softwareA.patient.dto.response.MedicalOrderResponse;
import com.softwareA.patient.model.medical_order.MedicalOrder;
import com.softwareA.patient.model.auth.AuthInfo;
import com.softwareA.patient.service.MedicalOrderService;
import com.softwareA.patient.utils.MedicalOrderPDFPrinter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medical-orders")
public class MedicalOrderController {
    private static final Logger log = LoggerFactory.getLogger(MedicalOrderController.class);
    private final MedicalOrderService medicalOrderService;
    private final MedicalOrderPDFPrinter medicalOrderPDFPrinter;

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

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<MedicalOrder>>> findMedicalOrders(@RequestHeader("UserRole") String userRole,
                                                                             @RequestHeader("UserId") String userId) {
        log.info("findMedicalOrders with data: \nUserRole: {}\nUserId: {}", userRole, userId);
        AuthInfo authInfo = AuthInfo.builder()
                .userId(userId)
                .userRole(userRole)
                .build();
        List<MedicalOrder> medicalOrders = this.medicalOrderService.findMedicalOrders(authInfo);
        return ResponseEntity.ok().body(ApiResponse.<List<MedicalOrder>>builder()
                .result(medicalOrders)
                .build());
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<ApiResponse<byte[]>> getMedicalOrderByIdPDF(@RequestHeader("UserRole") String userRole,
                                                                      @RequestHeader("UserId") String userId,
                                                                      @PathVariable String id) {
        log.info("getMedicalOrderByIdPDF with id: {}\nUserRole: {}\nUserId: {}", id, userRole, userId);
        AuthInfo authInfo = AuthInfo.builder()
                .userId(userId)
                .userRole(userRole)
                .build();
        MedicalOrderResponse medicalOrder = this.medicalOrderService.getMedicalOrderById(id, authInfo);
        // convert to pdf byte stream
        byte[] pdfResult = medicalOrderPDFPrinter.print(medicalOrder);
        return ResponseEntity.ok().body(ApiResponse.<byte[]>builder()
                .result(pdfResult)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicalOrderResponse>> getMedicalOrderById(@RequestHeader("UserRole") String userRole,
                                                                                 @RequestHeader("UserId") String userId,
                                                                                 @PathVariable String id) {
        log.info("Get medical order with id: {}\nUserRole: {}\nUserId: {}", id, userRole, userId);
        AuthInfo authInfo = AuthInfo.builder()
                .userId(userId)
                .userRole(userRole)
                .build();
        MedicalOrderResponse medicalOrder = this.medicalOrderService.getMedicalOrderById(id, authInfo);
        return ResponseEntity.ok().body(ApiResponse.<MedicalOrderResponse>builder()
                .result(medicalOrder)
                .build());
    }
}
