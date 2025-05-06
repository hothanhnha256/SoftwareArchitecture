package com.softwareA.appointment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GetAvailableDoctorsDTO {
    @NotNull(message = "departmentId cannot be null")
    String departmentId;
    @NotNull(message = "date cannot be null")
    LocalDate date;
    @NotNull(message = "hour cannot be null")
    Integer hour;

    public void validateDateRange() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime requestDateTime = date.atTime(hour, 0);

        if (now.isAfter(requestDateTime)) {
            throw new IllegalArgumentException("cannot get available doctors for past date");
        }
    }
}
