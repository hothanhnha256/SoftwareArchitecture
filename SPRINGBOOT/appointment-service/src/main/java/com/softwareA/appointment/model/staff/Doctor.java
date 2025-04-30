package com.softwareA.appointment.model.staff;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.softwareA.appointment.model.Department;
import lombok.Data;

@Data
public class Doctor {
    String id;
    String name;
    String role;
    String phoneNumber;
    String address;
    String departmentId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Department department;
    String jobTitle;
}
