package com.softwareA.patient.dto.request;

import com.softwareA.patient.model.BloodType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientDTO {
    String firstName;
    String lastName;
    LocalDate dob;
    String sex;
    String photoUrl;
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Invalid phone number. It should be 10 to 11 digits long.")
    String phoneNumber;
    @Email
    String email;
    String healthInsuranceNumber;
    String address;
    BloodType bloodType;
    String citizenId;
}
