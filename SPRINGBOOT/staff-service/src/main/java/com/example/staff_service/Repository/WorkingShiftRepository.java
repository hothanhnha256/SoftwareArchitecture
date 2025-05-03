package com.example.staff_service.Repository;

import com.example.staff_service.Entity.WorkingShift;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkingShiftRepository extends MongoRepository<WorkingShift, String> {
    // Truy vấn WorkingShift theo ngày và staffId
    List<WorkingShift> findByDateBetweenAndListStaffContaining(Date dateA, Date dateB, String staffId);

    // Truy vấn WorkingShift theo khoảng thời gian
    List<WorkingShift> findByDateBetween(Date dateA, Date dateB);

    @Query("{ 'date': { '$gte': ?0, '$lte': ?1 } }")
    List<WorkingShift> findByDateBetweenInclusive(Date dateA, Date dateB);

    // Truy vấn WorkingShift theo staffId
    List<WorkingShift> findByListStaffContaining(String staffId);

    @Query("{ 'date': { $gte: ?0, $lt: ?1 }, 'hours': ?2 }")
    Optional<WorkingShift> findOneByDateRangeAndHours(Date start, Date end, int hours);

    Optional<WorkingShift> findByDateAndHours(Date targetDate, int hour);
}
