package com.softwareA.appointment.client;

import com.softwareA.appointment.dto.request.GetAvailableDoctorsDTO;
import com.softwareA.appointment.model.staff.Doctor;
import com.softwareA.appointment.model.staff.Shift;
import com.softwareA.hospital.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "staff-service", url = "${staff-service.url}")
public interface StaffClient {
    @GetMapping("/department/{departmentId}")
    ApiResponse<List<Doctor>> getAvailableDoctors(@PathVariable("departmentId") String departmentId);
    @GetMapping("/{id}")
    ApiResponse<Doctor> getDoctorById(@RequestParam("id") String id);
    @GetMapping("/working-shifts/{id}")
    ApiResponse<Shift> getShiftById(@PathVariable("id") String id);
    @GetMapping("/working-shifts/get-shift-id")
    ApiResponse<Shift> getShiftByDateTime(@RequestParam("date") String date,
                                          @RequestParam("hours") Integer hour);

}
