package com.softwareA.hospital.controller;

import com.softwareA.hospital.dto.response.ApiResponse;
import com.softwareA.hospital.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final HospitalService hospitalService;

    @Autowired
    public PaymentController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PostMapping("/{option}")
    public ApiResponse<String> processPayment(@PathVariable String option, @RequestParam double amount) {
        return ApiResponse.<String>builder()
                .code(200)
                .message("Payment processed successfully")
                .result(hospitalService.processHospitalPayment(option, amount))
                .build();
    }
}
