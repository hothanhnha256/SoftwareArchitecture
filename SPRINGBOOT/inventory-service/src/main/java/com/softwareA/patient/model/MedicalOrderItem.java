package com.softwareA.patient.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Entity
@Data
public class MedicalOrderItem {
    @Id
    @NotNull(message = "code is required")
    String code;
    @Column(nullable = false)
    @NotNull(message = "name is required")
    String name;
    String description;
    @Column(nullable = false)
    @NotNull
    @PositiveOrZero(message = "Price must be 0 or greater")
    Double price;
}
