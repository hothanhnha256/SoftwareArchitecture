package com.softwareA.patient.service;

import com.softwareA.patient.client.StaffClient;
import com.softwareA.patient.dto.Staff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StaffServiceImpl implements IStaffService {
    private final StaffClient staffClient;

    //
    @Override
    public Staff getDoctorById(String id) {
        try {
            return staffClient.getDoctorById(id).getResult();
        } catch (Exception e) {
            log.error("Error when get doctor info: {}", e.getMessage());
        }
        return null;
    }
}
