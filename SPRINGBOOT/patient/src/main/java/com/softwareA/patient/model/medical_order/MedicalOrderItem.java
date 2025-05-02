package com.softwareA.patient.model.medical_order;

import lombok.Data;

@Data
public class MedicalOrderItem {
    String code;
    String name;
    String description;
    Double price;
}
