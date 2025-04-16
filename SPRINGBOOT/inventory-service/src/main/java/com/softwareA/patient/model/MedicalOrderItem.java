package com.softwareA.patient.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class MedicalOrderItem {
    @Id
    String code;
    @Column(nullable = false)
    String name;
    String description;
    @Column(nullable = false)
    @PositiveOrZero(message = "Price must be 0 or greater")
    Double price;
}
