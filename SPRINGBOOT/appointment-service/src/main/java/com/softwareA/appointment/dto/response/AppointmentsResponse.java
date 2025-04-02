package com.softwareA.appointment.dto.response;

import com.softwareA.appointment.model.appointment.Appointment;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class AppointmentsResponse {
    List<Appointment> appointments;
    Integer page;
    Integer limit;
    Integer total;
}
