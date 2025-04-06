package com.softwareA.hospital.dto.request;

import com.softwareA.hospital.model.BloodType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
