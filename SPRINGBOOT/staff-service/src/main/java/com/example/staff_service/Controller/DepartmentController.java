package com.example.staff_service.Controller;

import com.example.staff_service.DTO.Response.ApiResponse;
import com.example.staff_service.Entity.Department;
import com.example.staff_service.Service.DepartmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;  // final để Lombok có thể inject

    // Lấy tất cả các phòng ban
    @GetMapping
    public ApiResponse<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ApiResponse.<List<Department>>builder()
                .code(200)
                .message("Success")
                .result(departments)
                .build();
    }

    // Lấy phòng ban theo ID
    @GetMapping("/{id}")
    public ApiResponse<Department> getDepartmentById(@PathVariable String id) {
        return departmentService.getDepartmentById(id)
                .map(department -> ApiResponse.<Department>builder()
                        .code(200)
                        .message("Success")
                        .result(department)
                        .build())
                .orElseGet(() -> ApiResponse.<Department>builder()
                        .code(404)
                        .message("Department not found")
                        .result(null)
                        .build());
    }

    // Tạo phòng ban mới
    @PostMapping
    public ApiResponse<Department> createDepartment(@RequestBody Department department) {
        Department createdDepartment = departmentService.createDepartment(department);
        return ApiResponse.<Department>builder()
                .code(HttpStatus.CREATED.value())
                .message("Department created successfully")
                .result(createdDepartment)
                .build();
    }

    // Xóa phòng ban
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDepartment(@PathVariable String id) {
        departmentService.deleteDepartment(id);
        return ApiResponse.<Void>builder()
                .code(204)
                .message("Department deleted successfully")
                .result(null)
                .build();
    }
}
