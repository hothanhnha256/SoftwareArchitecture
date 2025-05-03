package com.softwareA.appointment.service;

import com.softwareA.appointment.model.staff.Shift;

import java.util.List;

public interface IShiftService {
    void checkIfShiftValid(Shift shift);

    Shift getShiftById(String shiftId);

    Shift getShiftByDateAndTime(String date, Integer hour);

    List<Shift> getShiftOverAPeriod(String startDate, String endDate);
}
