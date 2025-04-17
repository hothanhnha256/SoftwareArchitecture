package com.example.staff_service.Controller;

import com.example.staff_service.DTO.Response.ApiResponse;
import com.example.staff_service.Entity.Department;
import com.example.staff_service.Exception.ResourceNotFoundException;
import com.example.staff_service.Service.DepartmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    // Lấy tất cả các phòng ban
    @GetMapping
    public ResponseEntity<ApiResponse<List<Department>>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.<List<Department>>builder()
                .code(2000)
                .message("Success")
                .result(departments)
                .build());
    }

    // Lấy phòng ban theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> getDepartmentById(@PathVariable String id) {
        Department department = departmentService.getDepartmentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        return ResponseEntity.ok(ApiResponse.<Department>builder()
                .code(2000)
                .message("Success")
                .result(department)
                .build());
    }

    // Tạo phòng ban mới
    @PostMapping
    public ResponseEntity<ApiResponse<Department>> createDepartment(@RequestBody Department department) {
        Department createdDepartment = departmentService.createDepartment(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<Department>builder()
                .code(2010)
                .message("Department created successfully")
                .result(createdDepartment)
                .build());
    }

    // Xóa phòng ban
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable String id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.<Void>builder()
                .code(2040)
                .message("Department deleted successfully")
                .result(null)
                .build());
    }
}
