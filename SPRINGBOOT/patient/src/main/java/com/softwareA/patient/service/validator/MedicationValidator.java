package com.softwareA.patient.service.validator;

import com.softwareA.patient.client.InventoryClient;
import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.model.Medication;
import com.softwareA.patient.model.medical_order.MedicalOrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class MedicationValidator {
    private final InventoryClient inventoryClient;

    public void checkIfMedicationExists(List<String> medicationIds) {
        List<String> existingMedicationIds = inventoryClient.getAllMedicationsByIds(medicationIds).getResult()
                .stream()
                .map(Medication::getCode)
                .toList();

        // store existing ids in a set for faster lookup
        Set<String> existingIds = new HashSet<>(existingMedicationIds);
        for (String id : medicationIds) {
            if (!existingIds.contains(id)) {
                throw new AppException(ErrorCode.MEDICATION_NOT_FOUND, "Medication " + id + " not found");
            }
        }
    }

}
