package com.devteria.identity.feignclient;

import com.devteria.identity.dto.request.CreatePatientDTO;
import com.devteria.identity.dto.response.ApiResponse;
import com.devteria.identity.dto.response.PatientResponse;
import feign.Request;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "patient-service", url = "${feignclient.patient-service.url}")

public interface PatientServiceClient {
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<PatientResponse> createPatient(@Valid @RequestBody CreatePatientDTO request);
}

