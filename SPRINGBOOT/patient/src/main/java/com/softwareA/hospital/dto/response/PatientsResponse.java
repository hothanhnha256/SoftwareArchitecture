package com.softwareA.hospital.dto.response;

import com.softwareA.hospital.model.Patient;
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
