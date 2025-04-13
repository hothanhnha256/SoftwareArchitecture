package com.example.staff_service.Service;


import com.example.staff_service.Entity.WorkingShift;
import com.example.staff_service.Entity.Staff;
import com.example.staff_service.Repository.StaffRepository;
import com.example.staff_service.Repository.WorkingShiftRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WorkingShiftScheduler {
    private final WorkingShiftRepository workingShiftRepository;
    private final StaffRepository staffRepository;

    @Scheduled(cron = "0 0 0 ? * MON")
//    @Scheduled(cron = "0 * * * * ?")
    public void autoCreateWorkingShift() {
        // Ngày hôm nay + 7
        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE, 7);
        Date targetDate = normalizeDate(calendar.getTime()); // set về 00:00:00

        // Giờ làm việc: 7-11 và 13-15
        List<Integer> workingHours = new ArrayList<>();
        for (int hour = 7; hour <= 11; hour++) workingHours.add(hour);
        for (int hour = 13; hour <= 15; hour++) workingHours.add(hour);

        // Lấy tất cả ID nhân viên
        List<String> allStaffIds = staffRepository.findAll()
                .stream()
                .map(Staff::getId)
                .collect(Collectors.toList());
        // Tạo ca làm việc
        for (int hour : workingHours) {
            try {
                // Check nếu ca đã tồn tại thì skip
                Optional<WorkingShift> existing = workingShiftRepository.findByDateAndHours(targetDate, hour);
                if (existing.isPresent()) continue;

                WorkingShift newShift = new WorkingShift(targetDate, hour, allStaffIds);
                workingShiftRepository.save(newShift);
            } catch (Exception e) {
                System.out.println("❌ Không thể tạo shift cho giờ " + hour + ": " + e.getMessage());
            }
        }

        System.out.println("✅ Đã tạo xong ca làm việc cho ngày " + targetDate);
    }
    private Date normalizeDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
//    @PostConstruct
//    public void runOnStartup() {
//        // Gọi phương thức autoCreateWorkingShift() khi ứng dụng khởi động
//        autoCreateWorkingShift();
//    }
}
