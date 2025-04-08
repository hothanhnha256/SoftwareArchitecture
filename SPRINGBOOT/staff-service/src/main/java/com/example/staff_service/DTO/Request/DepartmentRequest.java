package com.example.staff_service.DTO.Request;

import lombok.Data;

@Data  // Lombok sẽ tự động tạo getter và setter cho các trường
public class DepartmentRequest {

    private String name;
    private String description;



    // Không cần khai báo getter và setter nếu dùng Lombok @Data
}
