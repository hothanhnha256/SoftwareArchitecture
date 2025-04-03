package com.example.staff_service.Controller;


import com.example.staff_service.Entity.WorkingShift;
import com.example.staff_service.Service.WorkingShiftService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/working-shifts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkingShiftController {
    WorkingShiftService workingShiftService;
    @PostMapping
    public WorkingShift createWorkingShift(@RequestBody WorkingShift request) {
        // Đầu vào từ request body sẽ được tự động map vào WorkingShiftRequest
        return workingShiftService.createWorkingShift(request.getDate(), request.getHours(), request.getListStaff());
    }
}
