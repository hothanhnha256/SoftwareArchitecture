package com.example.staff_service.Service;


import com.example.staff_service.DTO.Response.StaffResponse;
import com.example.staff_service.DTO.Response.WorkingShiftResponse;
import com.example.staff_service.Entity.Department;
import com.example.staff_service.Entity.Staff;
import com.example.staff_service.Entity.WorkingShift;
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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkingShiftService {
    WorkingShiftRepository workingShiftRepository;
    MongoTemplate mongoTemplate;


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
    public WorkingShift getWorkingShiftById(String workingShiftId) {
        return workingShiftRepository.findById(workingShiftId)
                .orElseThrow(() -> new RuntimeException("WorkingShift not found with id: " + workingShiftId));
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
    public List<StaffResponse> getStaffsInWorkingShift(Date date, int hours, String departmentId) {
        Date[] range = DateUtils.getStartAndEndOfDay(date);
        Date start = range[0];
        Date end = range[1];
        Optional<WorkingShift> optionalWorkingShift = workingShiftRepository.findOneByDateRangeAndHours(start, end, hours);

        if (optionalWorkingShift.isPresent()) {
            WorkingShift workingShift = optionalWorkingShift.get();

            // Lấy danh sách staffIds từ workingShift
            List<String> staffIds = workingShift.getListStaff();
            System.out.println("Staff IDs: " + staffIds);

            Query query = new Query();
            query.addCriteria(Criteria.where("id").in(staffIds)); // field "id" phải đúng với MongoDB
            List<Staff> staffList = mongoTemplate.find(query, Staff.class);
            System.out.println("Staff List: " + staffList);

            List<StaffResponse> staffResponses = new ArrayList<>();
            for (Staff staff : staffList) {
                if (departmentId != null && !departmentId.equals(staff.getDepartmentId())) {
                    continue;
                }
                StaffResponse staffResponse = new StaffResponse(
                        staff.getName(),
                        staff.getRole(),
                        staff.getPhoneNumber(),
                        staff.getAddress()
                );
                staffResponses.add(staffResponse);
            }

            return staffResponses;
        } else {
            throw new RuntimeException("WorkingShift not found with Date : " + date + " and Hours : " + hours);
        }
    }

}

