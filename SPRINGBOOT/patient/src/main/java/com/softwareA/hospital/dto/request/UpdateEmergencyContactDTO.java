package com.softwareA.hospital.dto.request;

import com.softwareA.hospital.model.EmergencyContactRelationship;

import lombok.*;

@Data
public class UpdateEmergencyContactDTO {
    String phoneNumber;
    String address;
    String firstName;
    String lastName;
    EmergencyContactRelationship relationship;
}
