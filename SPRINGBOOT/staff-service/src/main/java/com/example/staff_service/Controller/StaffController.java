package com.example.staff_service.Controller;


import com.example.staff_service.DTO.Response.ApiResponse;
import com.example.staff_service.Entity.Staff;
import com.example.staff_service.Service.StaffService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<Staff>> createStaff(@Valid @RequestBody Staff staff) {
        System.out.println(staff);
        Staff createdStaff = staffService.createStaff(staff);
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(ApiResponse.<Staff>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Staff created successfully")
                        .result(createdStaff)
                        .build());
    }

    // Cập nhật thông tin bác sĩ
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Staff>> updateStaff(@PathVariable String id, @RequestBody Staff staffDetails) {
        Staff updatedStaff = staffService.updateStaff(id, staffDetails);
        if (updatedStaff != null) {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.<Staff>builder()
                            .code(HttpStatus.OK.value())
                            .message("Staff updated successfully")
                            .result(updatedStaff)
                            .build());
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Staff>builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message("Staff not found")
                        .result(null)
                        .build());
    }

    // Xóa bác sĩ
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStaff(@PathVariable String id) {
        boolean isDeleted = staffService.deleteStaff(id);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.<Void>builder()
                            .code(HttpStatus.NO_CONTENT.value())
                            .message("Staff deleted successfully")
                            .result(null)
                            .build());
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Void>builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message("Staff not found")
                        .result(null)
                        .build());
    }
}
