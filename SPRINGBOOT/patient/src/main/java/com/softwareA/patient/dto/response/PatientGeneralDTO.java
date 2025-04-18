package com.softwareA.patient.dto.response;

import com.softwareA.patient.model.Sex;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientGeneralDTO {
    String id;
    String lastName;
    String firstName;
    LocalDate dob;
    Sex sex;
    String phoneNumber;
    String bloodType;
    String citizenId;
    Integer age;
    String healthInsuranceNumber;
    String email;
}
