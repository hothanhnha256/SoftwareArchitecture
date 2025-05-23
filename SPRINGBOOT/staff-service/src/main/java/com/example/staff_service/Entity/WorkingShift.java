package com.example.staff_service.Entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "workingShift")
@CompoundIndex(name = "unique_date_hours", def = "{'date' : 1, 'hours': 1}", unique = true)
public class WorkingShift {
    @Id
    String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Date date;

    int hours;

    List<String> listStaff;

    public WorkingShift(Date date, int hours, List<String> listStaff) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.hours = hours;
        this.listStaff = listStaff;
    }
}
