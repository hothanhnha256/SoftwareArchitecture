package com.softwareA.patient.service;

import com.softwareA.patient.model.Medication;
import com.softwareA.patient.model.prescription.Prescription_Medication;

import java.util.List;

public interface IMedicationService {
    List<Prescription_Medication> enrichMedicationInfos(List<Prescription_Medication> prescriptions);
}
