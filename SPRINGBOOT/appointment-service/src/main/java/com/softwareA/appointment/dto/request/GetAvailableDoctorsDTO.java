package com.softwareA.appointment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GetAvailableDoctorsDTO {
    @NotNull(message = "departmentId cannot be null")
    String departmentId;
    @NotNull (message = "date cannot be null")
    LocalDate date;
    @NotNull (message = "hour cannot be null")
    Integer hour;
}
