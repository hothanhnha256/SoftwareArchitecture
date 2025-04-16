package com.softwareA.patient.specification;

import com.softwareA.patient.model.MedicalOrderItem;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MedicalOrderItemSpecification {
    public static Specification<MedicalOrderItem> findWithFilters(String code, String name) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (code != null) {
                predicates.add(criteriaBuilder.like(root.get("code"), "%" + code + "%"));
            }
            if (name != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
