package com.softwareA.appointment.client;

import com.softwareA.appointment.model.Department;
import com.softwareA.patient.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "staff-department-service", url = "${staff-service.url}")
public interface DepartmentClient {
    @GetMapping("/departments")
    ApiResponse<List<Department>> getDepartments();
}
