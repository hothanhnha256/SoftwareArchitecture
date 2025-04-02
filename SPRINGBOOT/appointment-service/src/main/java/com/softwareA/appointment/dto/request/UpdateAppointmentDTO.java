package com.softwareA.appointment.dto.request;

import com.softwareA.appointment.model.appointment.AppointmentStatus;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateAppointmentDTO {
    AppointmentStatus status;
    String briefDescription;
}
