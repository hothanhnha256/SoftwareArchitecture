package com.softwareA.patient.dto.response;

import com.softwareA.patient.model.patient.Patient;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientResponse {
    Patient patient;
}
