package com.devteria.api_gateway.controller;

import com.devteria.api_gateway.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    private static final Logger log = LoggerFactory.getLogger(FallbackController.class);

    @GetMapping("/patient")
    public ResponseEntity<ApiResponse<Void>> patientFallback() {
        return ResponseEntity.status(500).body(
                ApiResponse.<Void>builder()
                        .message("Patient service is currently unavailable. Please try again later.")
                        .build());
    }

    @GetMapping("/identity")
    public ResponseEntity<ApiResponse<Void>> getIdentityFallback() {
        return ResponseEntity.status(500).body(
                ApiResponse.<Void>builder()
                        .message("Identity service is currently unavailable. Please try again later.")
                        .build());
    }

    @PostMapping("/identity")
    public ResponseEntity<ApiResponse<Void>> identityFallback() {
        log.info("identityFallback");
        return ResponseEntity.status(500).body(
                ApiResponse.<Void>builder()
                        .message("Identity service is currently unavailable. Please try again later.")
                        .build());
    }
}
