package com.softwareA.appointment.service;

import com.softwareA.appointment.client.StaffClient;
import com.softwareA.appointment.model.staff.Doctor;
import com.softwareA.patient.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.print.Doc;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffService implements IStaffService {
    private final StaffClient staffClient;

    @Override
    public Doctor getDoctorById(String staffId) {
        try {
            ApiResponse<Doctor> doctorResponse = staffClient.getDoctorById(staffId);
            return doctorResponse.getResult();
        } catch (Exception e) {
            // Handle the exception as needed
            log.error("Error fetching doctor by ID: {}", e.getMessage());
            return null;
        }
    }
}
