package com.example.staff_service.Repository;

import com.example.staff_service.Entity.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends MongoRepository<Staff, String> {

}
