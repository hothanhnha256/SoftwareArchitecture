package com.softwareA.appointment.client;


import com.softwareA.appointment.model.patient.Patient;
import com.softwareA.patient.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "${patient-service.url}", name = "patient-service")
public interface PatientClient {
    @GetMapping("/test-feign")
    ApiResponse<String> test();

    @GetMapping("/profile")
    ApiResponse<Patient> getPatientInfo(@RequestHeader("UserId") String userId, @RequestHeader("UserRole") String role);
}
