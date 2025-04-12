package com.example.staff_service.Controller;


import com.example.staff_service.DTO.Response.ApiResponse;
import com.example.staff_service.Entity.Staff;
import com.example.staff_service.Service.StaffService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffController {
    StaffService staffService;

    // Lấy tất cả các bác sĩ
    @GetMapping
    public ApiResponse<List<Staff>> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        return ApiResponse.<List<Staff>>builder()
                .code(200)
                .message("Success")
                .result(staffList)
                .build();
    }

    // Lấy bác sĩ theo ID
    @GetMapping("/{id}")
    public ApiResponse<Staff> getStaffById(@PathVariable String id) {
        Optional<Staff> staff = staffService.getStaffById(id);
        return staff.map(st -> ApiResponse.<Staff>builder()
                        .code(200)
                        .message("Success")
                        .result(st)
                        .build())
                .orElseGet(() -> ApiResponse.<Staff>builder()
                        .code(404)
                        .message("Staff not found")
                        .result(null)
                        .build());
    }
    @GetMapping("/department/{departmentId}")
    public ApiResponse<List<Staff>> getStaffByDepartmentId(@PathVariable String departmentId) {
        List<Staff> staffList = staffService.getStaffByDepartmentId(departmentId);
        return ApiResponse.<List<Staff>>builder()
                .code(200)
                .message("Success")
                .result(staffList)
                .build();
    }

    // Tạo mới bác sĩ
    @PostMapping("/create")
    public ApiResponse<Staff> createStaff(@RequestBody Staff staff) {
        System.out.println(staff);
        Staff createdStaff = staffService.createStaff(staff);
        return ApiResponse.<Staff>builder()
                .code(201)
                .message("Staff created successfully")
                .result(createdStaff)
                .build();
    }

    // Cập nhật thông tin bác sĩ
    @PutMapping("/{id}")
    public ApiResponse<Staff> updateStaff(@PathVariable String id, @RequestBody Staff staffDetails) {
        Staff updatedStaff = staffService.updateStaff(id, staffDetails);
        if (updatedStaff != null) {
            return ApiResponse.<Staff>builder()
                    .code(200)
                    .message("Staff updated successfully")
                    .result(updatedStaff)
                    .build();
        }
        return ApiResponse.<Staff>builder()
                .code(404)
                .message("Staff not found")
                .result(null)
                .build();
    }

    // Xóa bác sĩ
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStaff(@PathVariable String id) {
        boolean isDeleted = staffService.deleteStaff(id);
        if (isDeleted) {
            return ApiResponse.<Void>builder()
                    .code(204)
                    .message("Staff deleted successfully")
                    .result(null)
                    .build();
        }
        return ApiResponse.<Void>builder()
                .code(404)
                .message("Staff not found")
                .result(null)
                .build();
    }
}
