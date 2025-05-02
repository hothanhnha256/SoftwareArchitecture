package com.softwareA.patient.dto.response;

import com.softwareA.patient.model.patient.Patient;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientsResponse {
    Integer limit;
    Integer total;
    Integer page;
    List<Patient> patients;
}
