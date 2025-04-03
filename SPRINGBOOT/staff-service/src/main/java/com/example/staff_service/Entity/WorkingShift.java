package com.example.staff_service.Entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "workingShift")
public class WorkingShift {
    @Id
    String id;
    Date startTime;
    Date endTime;

    WorkingShift(Date startTime, Date endTime) {
        this.id = UUID.randomUUID().toString();
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
