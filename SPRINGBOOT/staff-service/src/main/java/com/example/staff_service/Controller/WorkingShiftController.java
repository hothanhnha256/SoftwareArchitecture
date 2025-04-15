package com.example.staff_service.Controller;


import com.example.staff_service.DTO.Request.StaffRequest;
import com.example.staff_service.DTO.Request.WorkingShiftRequest;
import com.example.staff_service.DTO.Response.ApiResponse;
import com.example.staff_service.DTO.Response.StaffResponse;
import com.example.staff_service.DTO.Response.WorkingShiftResponse;
import com.example.staff_service.Entity.WorkingShift;
import com.example.staff_service.Service.WorkingShiftService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<WorkingShift>> createWorkingShift(@RequestBody WorkingShiftRequest request) {
        WorkingShift workingShift = workingShiftService.createWorkingShift(
                request.getDate(),
                request.getHours(),
                request.getListStaff()
        );

        ApiResponse<WorkingShift> response = ApiResponse.<WorkingShift>builder()
                .code(HttpStatus.CREATED.value())
                .message("Working shift created successfully")
                .result(workingShift)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<WorkingShiftResponse>>> getAllWorkingShifts(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String staffId) {

        List<WorkingShiftResponse> workingShifts = workingShiftService.getWorkingShifts(startDate, endDate, staffId);

        ApiResponse<List<WorkingShiftResponse>> response = ApiResponse.<List<WorkingShiftResponse>>builder()
                .code(200)
                .message("Success")
                .result(workingShifts)
                .build();

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{workingShiftId}")
    public ResponseEntity<ApiResponse<WorkingShift>> getWorkingShiftById(@PathVariable String workingShiftId) {
        WorkingShift workingShift = workingShiftService.getWorkingShiftById(workingShiftId);

        ApiResponse<WorkingShift> response = ApiResponse.<WorkingShift>builder()
                .code(200)
                .message("Success")
                .result(workingShift)
                .build();
        return ResponseEntity.ok(response);
    }
    // Add Staff into Shift
    @PostMapping("/{workingShiftId}/add-staff")
    public ResponseEntity<ApiResponse<WorkingShift>> addStaffToWorkingShift(
            @PathVariable String workingShiftId,
            @RequestBody StaffRequest request) {

        WorkingShift updatedWorkingShift = workingShiftService.addStaffToWorkingShift(workingShiftId, request.getStaffId());

        ApiResponse<WorkingShift> response = ApiResponse.<WorkingShift>builder()
                .code(HttpStatus.CREATED.value())
                .message("Staff added successfully to the working shift.")
                .result(updatedWorkingShift)
                .build();

        return ResponseEntity.ok(response);
    }
    // Get Staff in Shift
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getStaffsInWorkingShift(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(required = false) Integer hours,
            @RequestParam(required = false) String departmentId
    ) {
        List<StaffResponse> staffs = workingShiftService.getStaffsInWorkingShift(date, hours, departmentId);

        ApiResponse<List<StaffResponse>> response = ApiResponse.<List<StaffResponse>>builder()
                .code(200)
                .message("Success")
                .result(staffs)
                .build();
        return ResponseEntity.ok(response);
    }
    // Get Shift Id by Date and Hours
    @GetMapping("/shift-id")
    public ResponseEntity<ApiResponse<WorkingShift>> getShiftIdByDateAndHours(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(required = false) int hours
    ) {
        WorkingShift workingShift = workingShiftService.getShiftIdByDateAndHours(date, hours);

        ApiResponse<WorkingShift> response = ApiResponse.<WorkingShift>builder()
                .code(200)
                .message("Success")
                .result(workingShift)
                .build();
        return ResponseEntity.ok(response);
    }

}
