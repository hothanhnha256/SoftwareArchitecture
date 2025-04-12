package com.softwareA.patient.dto.request;

import com.softwareA.patient.model.EmergencyContactRelationship;

import lombok.*;

@Data
public class UpdateEmergencyContactDTO {
    String phoneNumber;
    String address;
    String firstName;
    String lastName;
    EmergencyContactRelationship relationship;
}
