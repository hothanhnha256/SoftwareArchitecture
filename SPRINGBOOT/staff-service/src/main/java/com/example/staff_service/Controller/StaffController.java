package com.example.staff_service.Controller;


import com.example.staff_service.DTO.Response.ApiResponse;
import com.example.staff_service.Entity.Staff;
import com.example.staff_service.Service.StaffService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    public ResponseEntity<ApiResponse<List<Staff>>> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();

        if (staffList.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.<List<Staff>>builder()
                    .code(204)
                    .message("No staff found")
                    .result(null)
                    .build());
        }
        ApiResponse<List<Staff>> result = ApiResponse.<List<Staff>>builder()
                .code(200)
                .message("Success")
                .result(staffList)
                .build();
        return ResponseEntity.ok(result);
    }

    // Lấy bác sĩ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Staff>> getStaffById(@PathVariable String id) {
        Optional<Staff> staff = staffService.getStaffById(id);
        if (staff.isPresent()) {
            ApiResponse<Staff> response = ApiResponse.<Staff>builder()
                    .code(200)
                    .message("Success")
                    .result(staff.get())
                    .build();
            return ResponseEntity.ok(response);
        }
        ApiResponse<Staff> response = ApiResponse.<Staff>builder()
                .code(404)
                .message("Staff not found")
                .result(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<Staff>>> getStaffByDepartmentIdAndRole(@PathVariable String departmentId, @RequestParam(required = false) String role) {
        List<Staff> staffList;
        if(role == null){
            staffList = staffService.getStaffByDepartmentId(departmentId);
        }
        else {
            staffList = staffService.getStaffByDepartmentIdAndRole(departmentId, role);
        }
        if (staffList.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.<List<Staff>>builder()
                    .code(204)
                    .message("No staff found in this department")
                    .result(null)
                    .build());
        }
        ApiResponse<List<Staff>> result = ApiResponse.<List<Staff>>builder()
                .code(200)
                .message("Success")
                .result(staffList)
                .build();
        return ResponseEntity.ok(result);
    }

    // Tạo mới bác sĩ
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Staff>> createStaff(@RequestBody Staff staff) {
        System.out.println(staff);
        Staff createdStaff = staffService.createStaff(staff);
        ApiResponse<Staff> response = ApiResponse.<Staff>builder()
                .code(201)
                .message("Staff created successfully")
                .result(createdStaff)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Cập nhật thông tin bác sĩ
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Staff>> updateStaff(
            @PathVariable String id,
            @RequestBody Staff staffDetails
    ) {
        Staff updatedStaff = staffService.updateStaff(id, staffDetails);
        ApiResponse<Staff> response = ApiResponse.<Staff>builder()
                .code(2000)
                .message("Staff updated successfully")
                .result(updatedStaff)
                .build();

        return ResponseEntity.ok(response);
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
