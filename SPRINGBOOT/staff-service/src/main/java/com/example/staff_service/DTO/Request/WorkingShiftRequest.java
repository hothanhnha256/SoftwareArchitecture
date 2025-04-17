package com.example.staff_service.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkingShiftRequest {
    private Date date;
    private int hours;
    private List<String> listStaff;
}
