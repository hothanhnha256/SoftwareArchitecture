package com.softwareA.appointment.specification;

import com.softwareA.appointment.dto.request.GetAppointmentFilter;
import com.softwareA.appointment.model.appointment.Appointment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AppointmentSpecification {
    public static Specification<Appointment> getAppointmentsWithFilter(GetAppointmentFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getPatientIds() != null && !filter.getPatientIds().isEmpty()) {
                predicates.add(root.get("patientId").in(filter.getPatientIds()));
            }

            if (filter.getDoctorIds() != null && !filter.getDoctorIds().isEmpty()) {
                predicates.add(root.get("doctorId").in(filter.getDoctorIds()));
            }

            if (filter.getShiftIds() != null) {
                predicates.add(root.get("shiftId").in(filter.getShiftIds()));
            }

            if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

//    public static Specification<Appointment> getAppointmentsWithFilter(UUID patientId, UUID doctorId, List<UUID> shiftIds, String status) {
//        return (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            if (patientId != null) {
//                predicates.add(criteriaBuilder.equal(root.get("patientId"), patientId));
//            }
//
//            if (shiftIds != null && !shiftIds.isEmpty()) {
//                predicates.add(root.get("shiftId").in(shiftIds));
//            }
//
//            if (status != null && !status.isEmpty()) {
//                predicates.add(criteriaBuilder.equal(root.get("status"), status));
//            }
//
//            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//        };
//    }
}
