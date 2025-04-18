package com.softwareA.patient.dto.request;

import lombok.Data;

@Data
public class PatientSearchRequest {
    String firstName;
    String lastName;
    String phoneNumber;
    String citizenId;
    String healthInsuranceNumber;
    String email;
}
