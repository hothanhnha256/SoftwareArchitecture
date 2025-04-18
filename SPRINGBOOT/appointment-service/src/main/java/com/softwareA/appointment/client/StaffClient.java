package com.softwareA.appointment.client;

import com.softwareA.appointment.dto.request.GetAvailableDoctorsDTO;
import com.softwareA.appointment.model.staff.Doctor;
import com.softwareA.patient.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "staff-service", url = "${staff-service.url}")
public interface StaffClient {
    @GetMapping("/doctors/available")
    ApiResponse<List<Doctor>> getAvailableDoctors(GetAvailableDoctorsDTO dto, Pageable pageable);
}
