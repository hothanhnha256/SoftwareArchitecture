package com.softwareA.patient.service;

import com.softwareA.patient.dto.request.MedicalOrderItemRequestDTO;
import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.model.MedicalOrderItem;
import com.softwareA.patient.repository.MedicalOrderItemRepository;
import com.softwareA.patient.specification.MedicalOrderItemSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MedicalOrderItemService {
    private final MedicalOrderItemRepository medicalOrderItemRepository;

    public Page<MedicalOrderItem> getAllMedicalOrderItems(MedicalOrderItemRequestDTO dto, Pageable pageable) {
        Specification<MedicalOrderItem> spec = MedicalOrderItemSpecification.findWithFilters(dto.getCode(), dto.getName());
        return medicalOrderItemRepository.findAll(spec, pageable);
    }

    public MedicalOrderItem createMedicalOrderItem(MedicalOrderItem dto) {
        // Check if the medical order item already exists
        if (medicalOrderItemRepository.existsById(dto.getCode())) {
            throw new AppException(ErrorCode.CONFLICT, "Medical order item with ID " + dto.getCode() + " already exists.");
        }
        return medicalOrderItemRepository.save(dto);
    }

    public List<MedicalOrderItem> getAllMedicalOrderItemsByIds(List<String> ids) {
        return medicalOrderItemRepository.findAllById(ids);
    }
}
