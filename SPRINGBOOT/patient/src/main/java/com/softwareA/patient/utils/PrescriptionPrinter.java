package com.softwareA.patient.utils;

import com.softwareA.patient.dto.response.MedicalOrderResponse;
import com.softwareA.patient.dto.response.PrescriptionResponse;

public interface PrescriptionPrinter {
    byte[] print(PrescriptionResponse prescriptionResponse);
}
