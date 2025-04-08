package com.softwareA.hospital.model;

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
@Entity
@Builder
public class Patient {
    @Id
    UUID id;
    String lastName;
    @NotNull
    @Column(nullable = false)
    String firstName;
    @NotNull
    @Column(nullable = false)
    LocalDate dob;
    @NotNull
    @Enumerated(EnumType.STRING) // Store enum as a string
    @Column(nullable = false)
    Sex sex;
    String photoUrl;

    @NotNull
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Invalid phone number. It should be 10 to 11 digits long.")
    @Column(nullable = false)
    String phoneNumber;

    @Email
    String email;

    String healthInsuranceNumber; // mã số bảo hiểm y tế
    String address;
    @Enumerated(EnumType.STRING) // Store enum as a string
    BloodType bloodType;
    @NotNull
    @Column(nullable = false)
    String citizenId;
    @Transient // do not store age in database
    Integer age;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patientId") // This creates a foreign key in EmergencyContact
    private List<EmergencyContact> emergencyContacts;

    public Integer getAge() {
        return LocalDate.now().getYear() - dob.getYear();
    }
    // @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn(name = "patientId") // This creates a foreign key in
    // EmergencyContact
    // private MedicalRecord medicalRecord;
}
