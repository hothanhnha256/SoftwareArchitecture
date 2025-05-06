package com.softwareA.appointment.dto.response;

import com.softwareA.appointment.model.staff.Doctor;
import com.softwareA.appointment.model.staff.Shift;
import lombok.Data;

import java.util.List;

@Data
public class AvailableScheduleResponse {
    List<Doctor> doctors;
    Shift shift;
}
