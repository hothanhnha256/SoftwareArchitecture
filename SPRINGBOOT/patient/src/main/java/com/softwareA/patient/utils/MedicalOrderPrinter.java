package com.softwareA.patient.utils;

import com.softwareA.patient.dto.response.MedicalOrderResponse;
import com.softwareA.patient.model.MedicalOrder;

public interface MedicalOrderPrinter {
    byte[] print(MedicalOrderResponse medicalOrder);
}
