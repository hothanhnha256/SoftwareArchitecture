package com.softwareA.hospital.dto.response;

import com.softwareA.hospital.model.Patient;
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
