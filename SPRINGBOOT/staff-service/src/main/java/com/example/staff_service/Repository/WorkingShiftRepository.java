package com.example.staff_service.Repository;

import com.example.staff_service.Entity.WorkingShift;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WorkingShiftRepository extends MongoRepository<WorkingShift, String> {
    // Truy vấn WorkingShift theo ngày và staffId
    List<WorkingShift> findByDateBetweenAndListStaffContaining(Date dateA, Date dateB, String staffId);

    // Truy vấn WorkingShift theo khoảng thời gian
    List<WorkingShift> findByDateBetween(Date dateA, Date dateB);

    // Truy vấn WorkingShift theo staffId
    List<WorkingShift> findByListStaffContaining(String staffId);
}
