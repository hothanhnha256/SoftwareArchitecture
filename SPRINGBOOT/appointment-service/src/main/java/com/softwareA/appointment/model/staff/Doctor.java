package com.softwareA.appointment.model.staff;

import lombok.Data;

@Data
public class Doctor {
    String id;
    String name;
    String role;
    String phoneNumber;
    String address;
    String departmentId;
}
