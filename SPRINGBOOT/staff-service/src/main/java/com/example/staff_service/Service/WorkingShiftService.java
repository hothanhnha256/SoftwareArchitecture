package com.example.staff_service.Service;


import com.example.staff_service.DTO.Response.StaffResponse;
import com.example.staff_service.DTO.Response.WorkingShiftResponse;
import com.example.staff_service.Entity.Department;
import com.example.staff_service.Entity.Staff;
import com.example.staff_service.Entity.WorkingShift;
import com.example.staff_service.Exception.ResourceNotFoundException;
import com.example.staff_service.Repository.DepartmentRepository;
import com.example.staff_service.Repository.StaffRepository;
import com.example.staff_service.Repository.WorkingShiftRepository;
import com.example.staff_service.Utils.DateUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkingShiftService {
    WorkingShiftRepository workingShiftRepository;
    StaffRepository staffRepository;
    MongoTemplate mongoTemplate;


    public WorkingShift createWorkingShift(Date date, int hours, List<String> staffIds) {
        WorkingShift workingShift = new WorkingShift();
        workingShift.setDate(date);
        workingShift.setHours(hours);
        workingShift.setListStaff(staffIds);
        return workingShiftRepository.save(workingShift);
    }
    public List<WorkingShiftResponse> getWorkingShifts(String dateA, String dateB, String staffId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        if(staffId != null && staffRepository.findById(staffId).isEmpty()){
            throw new ResourceNotFoundException("Staff ID not found");
        }
        try {
            if (dateA != null && dateB != null) {
                startDate = sdf.parse(dateA);
                endDate = sdf.parse(dateB);
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd", e);
        }

        List<WorkingShift> workingShifts;

        if (startDate != null && endDate != null && staffId != null) {
            workingShifts = workingShiftRepository.findByDateBetweenAndListStaffContaining(startDate, endDate, staffId);
        } else if (startDate != null && endDate != null) {
            workingShifts = workingShiftRepository.findByDateBetween(startDate, endDate);
        } else if (staffId != null) {
            workingShifts = workingShiftRepository.findByListStaffContaining(staffId);
        } else {
            workingShifts = workingShiftRepository.findAll();
        }
        return workingShifts.stream()
                .map(shift -> new WorkingShiftResponse(shift.getDate(), shift.getHours()))
                .collect(Collectors.toList());
    }
    public WorkingShift getWorkingShiftById(String workingShiftId) {
        return workingShiftRepository.findById(workingShiftId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkingShift not found with id: " + workingShiftId));
    }
    public WorkingShift addStaffToWorkingShift(String workingShiftId, String staffId) {
        WorkingShift workingShift = workingShiftRepository.findById(workingShiftId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkingShift not found with id: " + workingShiftId));
        if (!staffRepository.existsById(staffId)) {
            throw new ResourceNotFoundException("Staff not found with id: " + staffId);
        }
        if (!workingShift.getListStaff().contains(staffId)) {
            workingShift.getListStaff().add(staffId);
        }
        return workingShiftRepository.save(workingShift);
    }
    public List<StaffResponse> getStaffsInWorkingShift(Date date, int hours, String departmentId) {
        Date[] range = DateUtils.getStartAndEndOfDay(date);
        Date start = range[0];
        Date end = range[1];

        WorkingShift workingShift = workingShiftRepository
                .findOneByDateRangeAndHours(start, end, hours)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WorkingShift not found for date: " + date + " and hours: " + hours));

        // Lấy danh sách staffIds từ workingShift
        List<String> staffIds = workingShift.getListStaff();

        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(staffIds));

        List<Staff> staffList = mongoTemplate.find(query, Staff.class);

        return staffList.stream()
                .filter(staff -> departmentId == null || departmentId.equals(staff.getDepartmentId()))
                .map(staff -> new StaffResponse(
                        staff.getName(),
                        staff.getRole(),
                        staff.getPhoneNumber(),
                        staff.getAddress()))
                .collect(Collectors.toList());
    }

    public WorkingShift getShiftIdByDateAndHours(Date date, int hours) {
        Date[] range = DateUtils.getStartAndEndOfDay(date);
        Date start = range[0];
        Date end = range[1];
        Optional<WorkingShift> optionalWorkingShift = workingShiftRepository.findOneByDateRangeAndHours(start, end, hours);

        if (optionalWorkingShift.isPresent()) {
            return optionalWorkingShift.get();
        } else {
            throw new RuntimeException("WorkingShift not found with Date : " + date + " and Hours : " + hours);
        }
    }

}

