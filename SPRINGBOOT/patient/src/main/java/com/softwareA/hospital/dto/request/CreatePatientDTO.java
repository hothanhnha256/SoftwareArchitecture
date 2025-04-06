package com.softwareA.hospital.dto.request;

import com.softwareA.hospital.model.BloodType;
import com.softwareA.hospital.model.Sex;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePatientDTO {
    @NotNull
    String firstName;

    String lastName;

    @NotNull
    LocalDate dob;

    @NotNull
    Sex sex;

    String photoUrl;

    @NotNull
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Invalid phone number. It should be 10 to 11 digits long.")
    String phoneNumber;

    @Email
    String email;

    String healthInsuranceNumber;
    @NotNull(message = "address cannot be null")
    String address;
    BloodType bloodType;

    @NotNull(message = "citizenId cannot be null")
    String citizenId;

}
