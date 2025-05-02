package com.softwareA.patient.service;

import com.softwareA.patient.dto.request.MedicationRequestDTO;
import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.model.Medication;
import com.softwareA.patient.repository.MedicationRepository;
import com.softwareA.patient.specification.MedicationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationService {
    private final MedicationRepository medicationRepository;


    public Medication createMedication(Medication medication) {
        if(medicationRepository.existsById(medication.getCode()))
        {
            throw new AppException(ErrorCode.CONFLICT, "Medication with code " + medication.getCode() + " already exists");
        }
        return medicationRepository.save(medication);
    }

    public Page<Medication> getAllMedications(MedicationRequestDTO dto, Pageable pageable) {
        Specification<Medication> spec = MedicationSpecification.findWithFilters(dto);
        return medicationRepository.findAll(spec, pageable);
    }

    public List<Medication> getAllMedicationsByIds(List<String> ids) {
        return medicationRepository.findAllById(ids);
    }

}
