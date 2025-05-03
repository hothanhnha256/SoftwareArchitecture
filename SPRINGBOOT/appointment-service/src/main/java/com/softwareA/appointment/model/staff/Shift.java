package com.softwareA.appointment.model.staff;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Shift {
    String id;
    OffsetDateTime date;
    int hours;
}
