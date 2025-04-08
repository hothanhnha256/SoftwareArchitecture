package com.example.staff_service.Repository;

import com.example.staff_service.DTO.Response.StaffResponse;
import com.example.staff_service.Entity.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends MongoRepository<Staff, String> {
    List<Staff> findByDepartmentId(String departmentId);
    List<Staff> findAllById(List<String> ids);

}
