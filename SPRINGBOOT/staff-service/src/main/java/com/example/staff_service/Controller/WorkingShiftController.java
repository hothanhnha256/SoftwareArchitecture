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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/{workingShiftId}/staffs")
    public ApiResponse<List<StaffResponse>> getStaffsInWorkingShift(@PathVariable String workingShiftId) {

        List<StaffResponse> staffs = workingShiftService.getStaffsInWorkingShift(workingShiftId);
        return ApiResponse.<List<StaffResponse>>builder()
                .code(200)
                .message("Success")
                .result(staffs)
                .build();
    }



}
