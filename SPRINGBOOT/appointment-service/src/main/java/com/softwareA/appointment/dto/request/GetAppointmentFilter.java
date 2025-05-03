package com.softwareA.appointment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAppointmentFilter {
    List<UUID> patientIds;
    List<String> doctorIds;
    List<String> shiftIds;
    String status;
}
