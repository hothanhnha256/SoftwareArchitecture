package com.example.staff_service.Entity;


import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Document(collection = "staff")
public class Staff {
    @Id
    String id;
    String name;
    String role;
    String phoneNumber;
    String address;

    @DBRef
    WorkingShift workingShiftId;
    @DBRef
    Department departmentId;



}
