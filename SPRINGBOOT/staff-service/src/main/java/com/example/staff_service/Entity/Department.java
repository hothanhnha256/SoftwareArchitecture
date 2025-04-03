package com.example.staff_service.Entity;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "department")
public class Department {
    @Id
    String id;
    String name;
    String description;


}
