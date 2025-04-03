package com.example.staff_service.Repository;

import com.example.staff_service.Entity.WorkingShift;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingShiftRepository extends MongoRepository<WorkingShift, String> {
}
