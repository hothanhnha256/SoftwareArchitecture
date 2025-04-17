package com.example.staff_service.Service;


import com.example.staff_service.Entity.Staff;
import com.example.staff_service.Exception.ResourceNotFoundException;
import com.example.staff_service.Repository.DepartmentRepository;
import com.example.staff_service.Repository.StaffRepository;
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
    DepartmentRepository departmentRepository;


    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }


    public Optional<Staff> getStaffById(String id) {
        return staffRepository.findById(id);
    }
    public List<Staff> getStaffByDepartmentId(String departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department not found with id: " + departmentId);
        }
        return staffRepository.findByDepartmentId(departmentId);
    }


    public Staff createStaff(Staff staff) {
        return staffRepository.save(staff);
    }


    public Staff updateStaff(String id, Staff staffDetails) {
        Staff existing = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));

        existing.setName(staffDetails.getName());
        existing.setRole(staffDetails.getRole());
        existing.setPhoneNumber(staffDetails.getPhoneNumber());
        existing.setAddress(staffDetails.getAddress());
        existing.setDepartmentId(staffDetails.getDepartmentId());

        return staffRepository.save(existing);
    }


    public boolean deleteStaff(String id) {
        if (staffRepository.existsById(id)) {
            staffRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
