package com.softwareA.patient.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String sex;

    String photoUrl;

    @NotNull
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Invalid phone number. It should be 10 to 11 digits long.")
    String phoneNumber;

    @Email
    String email;

    String healthInsuranceNumber;
    String address;
    String bloodType;

    @NotNull
    String citizenId;

}
