package com.softwareA.appointment.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class GetAppointmentFilter {
    List<UUID> patientIds;
    List<UUID> doctorIds;
    List<String> shiftIds;
    String status;
}
