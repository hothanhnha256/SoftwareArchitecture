package com.softwareA.appointment.dto.request;

import com.softwareA.appointment.model.appointment.AppointmentStatus;
import lombok.Data;

import java.time.LocalDate;
@Data
public class GetAppointmentsDTO {
    String startDate = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String endDate = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    AppointmentStatus status;
    Boolean includeDetail = false;
}
