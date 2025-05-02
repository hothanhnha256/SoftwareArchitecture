package com.softwareA.patient.specification;

import com.softwareA.patient.dto.request.PatientSearchRequest;
import com.softwareA.patient.model.patient.Patient;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class PatientSpecification {
    public static Specification<Patient> withFilters(PatientSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getCitizenId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("citizenId"), request.getCitizenId()));
            }
            if (request.getEmail() != null) {
                predicates.add(criteriaBuilder.equal(root.get("email"), request.getEmail()));
            }
            if (request.getFirstName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + request.getFirstName().toLowerCase() + "%"));
            }
            if (request.getLastName() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + request.getLastName().toLowerCase() + "%"));
            }
            if (request.getHealthInsuranceNumber() != null) {
                predicates.add(criteriaBuilder.equal(root.get("healthInsuranceNumber"), request.getHealthInsuranceNumber()));
            }
            if (request.getPhoneNumber() != null) {
                predicates.add(criteriaBuilder.equal(root.get("phoneNumber"), request.getPhoneNumber()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
