package com.softwareA.appointment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GetAvailableDoctorsDTO {
    @NotNull(message = "departmentId cannot be null")
    String departmentId;
    @NotNull(message = "shiftId cannot be null")
    String shiftId;
}
