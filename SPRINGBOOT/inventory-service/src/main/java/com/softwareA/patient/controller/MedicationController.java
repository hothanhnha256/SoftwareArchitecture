package com.softwareA.patient.controller;

import com.softwareA.patient.dto.request.MedicationRequestDTO;
import com.softwareA.patient.dto.response.ApiResponse;
import com.softwareA.patient.model.Medication;
import com.softwareA.patient.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Medication>>> findMedications(MedicationRequestDTO dto,
                                                                         @PageableDefault(size = 50) Pageable pageable) {
        Page<Medication> medicationPage = medicationService.getAllMedications(dto, pageable);

        return ResponseEntity.ok().body(ApiResponse.<List<Medication>>builder()
                .result(medicationPage.getContent())
                .page(medicationPage.getNumber())
                .limit(medicationPage.getSize())
                .total(medicationPage.getTotalPages())
                .build());
    }


    @PostMapping("")
    public ResponseEntity<ApiResponse<Medication>> createMedication(@Valid @RequestBody Medication medication) {
        Medication created = medicationService.createMedication(medication);

        return ResponseEntity.ok().body(ApiResponse.<Medication>builder()
                .result(created)
                .build());
    }

    @PostMapping("/by-ids")
    public ResponseEntity<ApiResponse<List<Medication>>> getAllMedicationsByIds(@RequestBody List<String> ids) {
        List<Medication> medications = medicationService.getAllMedicationsByIds(ids);

        return ResponseEntity.ok().body(ApiResponse.<List<Medication>>builder()
                .result(medications)
                .build());
    }
}
