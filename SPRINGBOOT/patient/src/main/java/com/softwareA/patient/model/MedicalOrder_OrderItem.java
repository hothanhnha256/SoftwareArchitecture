package com.softwareA.patient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"medicalOrderId", "medicalOrderItemId"})
})
@Data
public class MedicalOrder_OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(nullable = false)
    String medicalOrderId;
    @Column(nullable = false)
    String medicalOrderItemId;
    @Column(nullable = false)
    @Positive
    Integer quantity;
    @Enumerated(EnumType.STRING)
    MedicalOrderItemStatus status = MedicalOrderItemStatus.PENDING;
    @Transient
    MedicalOrderItem medicalOrderItem;
}
