package com.softwareA.patient.client;

import com.softwareA.patient.dto.Staff;
import com.softwareA.patient.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "staff-service", url = "${staff-service.url}")
public interface StaffClient {
    @GetMapping("/{id}")
    ApiResponse<Staff> getDoctorById(@RequestParam("id") String id);
}
