package com.softwareA.patient.utils;

import com.softwareA.patient.dto.response.MedicalOrderResponse;

public interface MedicalOrderPrinter {
    byte[] print(MedicalOrderResponse medicalOrder);
}
