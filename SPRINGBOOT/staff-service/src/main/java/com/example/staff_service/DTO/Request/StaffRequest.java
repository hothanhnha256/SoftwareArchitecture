package com.example.staff_service.DTO.Request;


import lombok.Data;

@Data
public class StaffRequest {
    private String staffId;

    // Không cần khai báo getter và setter nếu dùng Lombok @Data
}
