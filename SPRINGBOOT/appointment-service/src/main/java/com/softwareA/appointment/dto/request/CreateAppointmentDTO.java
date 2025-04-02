package com.softwareA.appointment.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateAppointmentDTO {
    @NotNull(message = "doctorId cannot be null")
    String doctorId;
    @NotNull(message = "date cannot be null")
    @NotNull(message = "shiftId cannot be null")
    String shiftId;
    String briefDescription;

}
