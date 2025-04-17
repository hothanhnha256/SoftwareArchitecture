package com.example.staff_service.Service;


import com.example.staff_service.Entity.Staff;
import com.example.staff_service.Repository.StaffRepository;
import com.example.staff_service.exception.AppException;
import com.example.staff_service.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffService {
    StaffRepository staffRepository;


    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }


    public Optional<Staff> getStaffById(String id) {
        return staffRepository.findById(id);
    }
    public List<Staff> getStaffByDepartmentId(String departmentId) {
        return staffRepository.findByDepartmentId(departmentId);
    }


    public Staff createStaff(Staff staff) {
        System.out.println(staff.getDepartmentId());
        this.getStaffById(staff.getId()).ifPresent(existingStaff -> {
            throw new AppException(ErrorCode.CONFLICT, "Staff with ID " + staff.getId() + " already exists.");
        });
        return staffRepository.save(staff);
    }


    public Staff updateStaff(String id, Staff staffDetails) {
        if (staffRepository.existsById(id)) {
            staffDetails.setId(id);
            return staffRepository.save(staffDetails);
        }
        return null;
    }


    public boolean deleteStaff(String id) {
        if (staffRepository.existsById(id)) {
            staffRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
