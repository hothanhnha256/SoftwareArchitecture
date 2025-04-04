package com.example.staff_service.Service;


import com.example.staff_service.Entity.WorkingShift;
import com.example.staff_service.Repository.WorkingShiftRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkingShiftService {
    WorkingShiftRepository workingShiftRepository;


    public WorkingShift createWorkingShift(Date date, int hours, List<String> staffIds) {
        WorkingShift workingShift = new WorkingShift();
        workingShift.setDate(date);
        workingShift.setHours(hours);
        workingShift.setListStaff(staffIds);
        return workingShiftRepository.save(workingShift);
    }
    public List<WorkingShift> getWorkingShifts(String dateA, String dateB, String listStaff) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(dateA);
            endDate = sdf.parse(dateB);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (startDate != null && endDate != null && listStaff != null) {
            return workingShiftRepository.findByDateBetweenAndListStaffContaining(startDate, endDate, listStaff);
        } else if (startDate != null && endDate != null) {
            return workingShiftRepository.findByDateBetween(startDate, endDate);
        } else if (listStaff != null) {
            return workingShiftRepository.findByListStaffContaining(listStaff);
        } else {
            return workingShiftRepository.findAll();
        }
    }
}
