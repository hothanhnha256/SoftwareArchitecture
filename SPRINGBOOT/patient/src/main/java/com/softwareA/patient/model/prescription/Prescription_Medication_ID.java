package com.softwareA.patient.model.prescription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription_Medication_ID implements Serializable {
    private String prescriptionId;
    private String medicationCode;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prescription_Medication_ID)) return false;
        Prescription_Medication_ID that = (Prescription_Medication_ID) o;
        return Objects.equals(prescriptionId, that.prescriptionId) &&
                Objects.equals(medicationCode, that.medicationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prescriptionId, medicationCode);
    }
}
