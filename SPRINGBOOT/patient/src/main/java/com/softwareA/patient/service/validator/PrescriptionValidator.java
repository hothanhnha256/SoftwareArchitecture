package com.softwareA.patient.service.validator;

import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.model.auth.AuthInfo;
import com.softwareA.patient.model.medical_order.MedicalOrder;
import com.softwareA.patient.model.prescription.Prescription;
import com.softwareA.patient.model.prescription.PrescriptionStatus;
import com.softwareA.patient.repository.MedicalOrderRepository;
import com.softwareA.patient.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PrescriptionValidator {
    private final PrescriptionRepository prescriptionRepository;
    private final MedicalOrderRepository medicalOrderRepository;

    public boolean checkIfPrescriptionOfMedicalOrderExists(String medicalOrderId) {
        List<Prescription> prescriptions = prescriptionRepository.findByMedicalOrderId(medicalOrderId);
        return prescriptions.stream().anyMatch(prescription -> !PrescriptionStatus.CANCELLED.toString()
                .equalsIgnoreCase(prescription.getStatus().toString()));
    }

    public void validateAccess(Prescription prescription, AuthInfo authInfo) {
        switch (authInfo.getUserRole()) {
            case "DOCTOR":
            case "PHARMACIST":
                break;
            case "USER":
                Optional<MedicalOrder> medicalOrderOptional = medicalOrderRepository.findById(prescription.getMedicalOrderId());
                if (medicalOrderOptional.isEmpty()) {
                    throw new AppException(ErrorCode.MEDICAL_ORDER_NOT_FOUND, "Medical order linked to this prescription not found");
                }
                if (!medicalOrderOptional.get().getPatientId().toString().equals(authInfo.getUserId())) {
                    throw new AppException(ErrorCode.FORBIDDEN, "You are not authorized to access this prescription");
                }
                break;
            default:
                throw new AppException(ErrorCode.UNAUTHORIZED, "Unauthorized access");
        }
    }
}
