package com.example.staff_service.Controller;


import com.example.staff_service.DTO.Request.StaffRequest;
import com.example.staff_service.DTO.Response.ApiResponse;
import com.example.staff_service.DTO.Response.StaffResponse;
import com.example.staff_service.DTO.Response.WorkingShiftResponse;
import com.example.staff_service.Entity.WorkingShift;
import com.example.staff_service.Service.WorkingShiftService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/working-shifts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkingShiftController {
    WorkingShiftService workingShiftService;
    @PostMapping
    public ApiResponse<WorkingShift> createWorkingShift(@RequestBody WorkingShift request) {

        // Đầu vào từ request body sẽ được tự động map vào WorkingShiftRequest
        WorkingShift workingShift = workingShiftService.createWorkingShift(request.getDate(), request.getHours(), request.getListStaff());
        return ApiResponse.<WorkingShift>builder()
                .code(201)
                .message("Success")
                .result(workingShift)
                .build();
    }
    @GetMapping("")
    public ApiResponse<List<WorkingShiftResponse>> getAllWorkingShifts(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String listStaff) {

        List<WorkingShiftResponse> workingShifts = workingShiftService.getWorkingShifts(startDate, endDate, listStaff);

        return ApiResponse.<List<WorkingShiftResponse>>builder()
                .code(200)
                .message("Success")
                .result(workingShifts)
                .build();
    }
    @GetMapping("/{workingShiftId}")
    public ApiResponse<WorkingShift> getWorkingShiftById(@PathVariable String workingShiftId) {
        WorkingShift workingShift = workingShiftService.getWorkingShiftById(workingShiftId);
        return ApiResponse.<WorkingShift>builder()
                .code(200)
                .message("Success")
                .result(workingShift)
                .build();
    }
    // Add Staff into Shift
    @PostMapping("/{workingShiftId}/add-staff")
    public ApiResponse<WorkingShift> addStaffToWorkingShift(
            @PathVariable String workingShiftId,
            @RequestBody StaffRequest request) {
        WorkingShift updatedWorkingShift = workingShiftService.addStaffToWorkingShift(workingShiftId, request.getStaffId());

        return ApiResponse.<WorkingShift>builder()
                        .code(200)
                        .message("Staff added successfully to the working shift.")
                        .result(updatedWorkingShift)
                        .build();
    }
    // Get Staff in Shift
    @GetMapping("/available")
    public ApiResponse<List<StaffResponse>> getStaffsInWorkingShift(
//            @RequestParam(required = false) String workingShiftId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(required = false) int hours,
            @RequestParam(required = false) String departmentId
    ) {
        System.out.println("Date: " + date);
        System.out.println("Hours: " + hours);

        List<StaffResponse> staffs = workingShiftService.getStaffsInWorkingShift(date, hours, departmentId);
        return ApiResponse.<List<StaffResponse>>builder()
                .code(200)
                .message("Success")
                .result(staffs)
                .build();
    }
    // Get Shift Id by Date and Hours
    @GetMapping("/get-shift-id")
    public ApiResponse<WorkingShift> getShiftIdByDateAndHours(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(required = false) int hours
    ) {
        WorkingShift workingShift = workingShiftService.getShiftIdByDateAndHours(date, hours);
        return ApiResponse.<WorkingShift>builder()
                .code(200)
                .message("Success")
                .result(workingShift)
                .build();
    }

}
