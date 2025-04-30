package com.devteria.identity.feignclient;

import jakarta.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.devteria.identity.dto.request.CreatePatientDTO;
import com.devteria.identity.dto.response.ApiResponse;
import com.devteria.identity.dto.response.PatientResponse;

@FeignClient(name = "patient-service", url = "${feignclient.patient-service.url}")
public interface PatientServiceClient {
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<PatientResponse> createPatient(@Valid @RequestBody CreatePatientDTO request);
}
