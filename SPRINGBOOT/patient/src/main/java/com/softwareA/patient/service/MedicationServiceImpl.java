package com.softwareA.patient.service;

import com.softwareA.patient.client.InventoryClient;
import com.softwareA.patient.model.Medication;
import com.softwareA.patient.model.prescription.Prescription_Medication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicationServiceImpl implements IMedicationService {
    private final InventoryClient inventoryClient;

    @Override
    public List<Prescription_Medication> enrichMedicationInfos(List<Prescription_Medication> mapper) {
        try {
            List<Medication> medications = inventoryClient.getAllMedicationsByIds(mapper.stream().map(Prescription_Medication::getMedicationCode).toList()).getResult();
            mapper = mapper.stream().peek(medication -> {
                Medication med = medications.stream()
                        .filter(m -> m.getCode().equals(medication.getMedicationCode()))
                        .findFirst()
                        .orElse(null);
                if (med != null) {
                    medication.setName(med.getName());
                    medication.setManufacturer(med.getManufacturer());
                    medication.setDosageForm(med.getDosageForm());
                    medication.setStrength(med.getStrength());
                    medication.setDescription(med.getDescription());
                    medication.setPrice(med.getPrice());
                }
            }).toList();
        } catch (Exception e) {
            log.error("Error when get medication info: {}", e.getMessage());
        }
        return mapper;

    }

}
