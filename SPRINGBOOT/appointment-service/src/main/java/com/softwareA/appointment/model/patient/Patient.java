package com.softwareA.appointment.model.patient;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Patient {
    UUID id;
    String lastName;
    String firstName;
    LocalDate dob;
    Sex sex;
    String photoUrl;

    String phoneNumber;

    String email;

    String healthInsuranceNumber; // mã số bảo hiểm y tế
    String address;
    BloodType bloodType;
    String citizenId;
    Integer age;

}
