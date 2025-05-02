package com.softwareA.patient.service;

import com.softwareA.patient.client.InventoryClient;
import com.softwareA.patient.client.StaffClient;
import com.softwareA.patient.dto.Staff;
import com.softwareA.patient.dto.request.CreatePrescriptionRequest;
import com.softwareA.patient.dto.request.MedicationRequest;
import com.softwareA.patient.dto.response.PrescriptionResponse;
import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.mapper.PrescriptionMapper;
import com.softwareA.patient.model.Medication;
import com.softwareA.patient.model.auth.AuthInfo;
import com.softwareA.patient.model.medical_order.MedicalOrder;
import com.softwareA.patient.model.prescription.Prescription;
import com.softwareA.patient.model.prescription.Prescription_Medication;
import com.softwareA.patient.repository.MedicalOrderRepository;
import com.softwareA.patient.repository.PrescriptionRepository;
import com.softwareA.patient.repository.Prescription_MedicationRepository;
import com.softwareA.patient.service.validator.MedicalOrderValidator;
import com.softwareA.patient.service.validator.MedicationValidator;
import com.softwareA.patient.service.validator.PatientValidator;
import com.softwareA.patient.service.validator.PrescriptionValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrescriptionService {
    private static final Logger log = LoggerFactory.getLogger(PrescriptionService.class);
    private final PrescriptionRepository prescriptionRepository;
    private final MedicalOrderRepository medicalOrderRepository;
    private final Prescription_MedicationRepository prescription_medicationRepository;
    private final MedicalOrderValidator medicalOrderValidator;
    private final PrescriptionValidator prescriptionValidator;
    private final MedicationValidator medicationValidator;
    private final PrescriptionMapper prescriptionMapper;
    private final IMedicationService medicationService;
    private final IStaffService staffService;

    @Transactional
    public PrescriptionResponse createPrescription(CreatePrescriptionRequest request, AuthInfo authInfo) {
        // only doctor can create prescription
        if (!authInfo.getUserRole().equals("DOCTOR")) {
            throw new AppException(ErrorCode.FORBIDDEN, "Only doctor can create prescription");
        }

        // check if medical order exists
        if (!medicalOrderValidator.checkIfMedicalOrderExists(request.getMedicalOrderId())) {
            throw new AppException(ErrorCode.MEDICAL_ORDER_NOT_FOUND);
        }
        // check if prescription already exists for this medical order
        if (prescriptionValidator.checkIfPrescriptionOfMedicalOrderExists(request.getMedicalOrderId())) {
            throw new AppException(ErrorCode.PRESCRIPTION_EXISTS, "Prescription already exists for this medical order");
        }

        Prescription prescription = new Prescription();
        prescription.setDoctorId(authInfo.getUserId());
        prescription.setMedicalOrderId(request.getMedicalOrderId());
        Prescription createdPrescription = prescriptionRepository.save(prescription);


        List<Prescription_Medication> entities = createPrescriptionMedications(request.getMedications(), createdPrescription.getId());
        prescription_medicationRepository.saveAll(entities);

        PrescriptionResponse response = prescriptionMapper.toPrescriptionDetailDTO(createdPrescription);
        // add doctor info to prescription
        response.setDoctor(staffService.getDoctorById(response.getDoctorId()));
        // add medication info to prescription
        response.setMedications(entities);
        return response;
    }

    private List<Prescription_Medication> createPrescriptionMedications(List<MedicationRequest> medications, String prescriptionId) {

        medicationValidator.checkIfMedicationExists(medications.stream()
                .map(MedicationRequest::getId)
                .toList());

        List<Prescription_Medication> entities = new ArrayList<>();
        for (MedicationRequest med : medications) {
            Prescription_Medication entity = new Prescription_Medication();
            entity.setPrescriptionId(prescriptionId);
            entity.setMedicationCode(med.getId());
            entity.setQuantity(med.getQuantity());
            entities.add(entity);
        }
        return entities;
    }


//    public Prescription updatePrescription(AuthInfo authInfo) {
//        // doctor update the prescription info
//
//    }

    public PrescriptionResponse getPrescriptionDetail(String prescriptionId, AuthInfo authInfo) {

        Optional<Prescription> presciptionOptional = prescriptionRepository.findById(prescriptionId);
        if (presciptionOptional.isEmpty()) {
            throw new AppException(ErrorCode.PRESCRIPTION_NOT_FOUND, "Prescription " + prescriptionId + " not found");
        }
        // validate access
        prescriptionValidator.validateAccess(presciptionOptional.get(), authInfo);
        PrescriptionResponse response = prescriptionMapper.toPrescriptionDetailDTO(presciptionOptional.get());
        List<Prescription_Medication> medications = prescription_medicationRepository.findAllByPrescriptionId(prescriptionId);
        // enrich medication info
        medications = medicationService.enrichMedicationInfos(medications);
        response.setMedications(medications);
        // add doctor info to prescription
        response.setDoctor(staffService.getDoctorById(response.getDoctorId()));
        return response;
    }

    public List<Prescription> getPrescriptions(AuthInfo authInfo) {
        // doctor get the prescription info
        if (authInfo.getUserRole().equals("DOCTOR")) {
            return prescriptionRepository.findByDoctorId(authInfo.getUserId());
        }
        // pharmacist get the prescription info
        if (authInfo.getUserRole().equals("PHARMACIST")) {
            return prescriptionRepository.findByPharmacistId(authInfo.getUserId());
        }

        // pharmacist get the prescription info
        if (authInfo.getUserRole().equals("USER")) {
            List<MedicalOrder> medicalOrders = medicalOrderRepository.findByPatientId(UUID.fromString(authInfo.getUserId()));
            return prescriptionRepository.findAllByMedicalOrderIdIn(medicalOrders.stream()
                    .map(MedicalOrder::getId)
                    .toList());
        }


        return new ArrayList<>();
    }

}
