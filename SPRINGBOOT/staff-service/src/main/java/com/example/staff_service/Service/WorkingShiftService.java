package com.example.staff_service.Service;


import com.example.staff_service.DTO.Response.WorkingShiftResponse;
import com.example.staff_service.Entity.WorkingShift;
import com.example.staff_service.Repository.WorkingShiftRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<WorkingShiftResponse> getWorkingShifts(String dateA, String dateB, String listStaff) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(dateA);
            endDate = sdf.parse(dateB);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<WorkingShift> workingShifts;
        if (startDate != null && endDate != null && listStaff != null) {
            workingShifts =  workingShiftRepository.findByDateBetweenAndListStaffContaining(startDate, endDate, listStaff);
        } else if (startDate != null && endDate != null) {
            workingShifts = workingShiftRepository.findByDateBetween(startDate, endDate);
        } else if (listStaff != null) {
            workingShifts = workingShiftRepository.findByListStaffContaining(listStaff);
        } else {
            workingShifts = workingShiftRepository.findAll();
        }
        return workingShifts.stream()
                .map(shift -> new WorkingShiftResponse(shift.getDate(), shift.getHours()))
                .collect(Collectors.toList());
    }
    public WorkingShift addStaffToWorkingShift(String workingShiftId, String staffId) {
        Optional<WorkingShift> optionalWorkingShift = workingShiftRepository.findById(workingShiftId);

        if (optionalWorkingShift.isPresent()) {
            WorkingShift workingShift = optionalWorkingShift.get();
            if (!workingShift.getListStaff().contains(staffId)) workingShift.getListStaff().add(staffId);
            return workingShiftRepository.save(workingShift);
        } else {
            throw new RuntimeException("WorkingShift not found with id: " + workingShiftId);
        }
    }
}
