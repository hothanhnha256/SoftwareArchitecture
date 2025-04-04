package com.example.staff_service.Controller;


import com.example.staff_service.DTO.Response.ApiResponse;
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
    public ApiResponse<List<WorkingShift>> getAllWorkingShifts(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String listStaff) {

        List<WorkingShift> workingShifts = workingShiftService.getWorkingShifts(startDate, endDate, listStaff);

        return ApiResponse.<List<WorkingShift>>builder()
                .code(200)
                .message("Success")
                .result(workingShifts)
                .build();
    }

}
