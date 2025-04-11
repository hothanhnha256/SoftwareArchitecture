package com.softwareA.appointment.model.staff;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Shift {
    String id;
    Date date;
    int hours;
}
