package com.softwareA.appointment.service;

import com.softwareA.appointment.client.StaffClient;
import com.softwareA.appointment.exception.AppException;
import com.softwareA.appointment.exception.ErrorCode;
import com.softwareA.appointment.model.staff.Shift;
import com.softwareA.patient.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShiftService implements IShiftService {

    private final StaffClient staffClient;

    @Override
    public List<Shift> getShiftOverAPeriod(String startDate, String endDate) {
        try {
            ApiResponse<List<Shift>> shiftResponse = staffClient.getShiftOverAPeriod(startDate, endDate);
            return shiftResponse.getResult();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Shift getShiftByDateAndTime(String date, Integer hour) {
        try {
            ApiResponse<Shift> shiftResponse = this.staffClient.getShiftByDateTime(date, hour);
            return shiftResponse.getResult();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Shift getShiftById(String shiftId) {
        try {
            ApiResponse<Shift> shiftResponse = staffClient.getShiftById(shiftId);
            return shiftResponse.getResult();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void checkIfShiftValid(Shift shift) {
        if (shift == null) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Shift is null");
        }
        OffsetDateTime actualShiftTime = shift.getDate().plusHours(shift.getHours());
        log.info("Shift time: {}", actualShiftTime);
        log.info("Now: {}", OffsetDateTime.now());

        if (actualShiftTime.isBefore(OffsetDateTime.now())) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Shift date is in the past");
        }
    }
}
