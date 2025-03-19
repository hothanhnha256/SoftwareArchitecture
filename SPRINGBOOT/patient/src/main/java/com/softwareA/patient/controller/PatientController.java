package com.softwareA.patient.controller;

import com.softwareA.patient.dto.response.ApiResponse;
import com.softwareA.patient.dto.response.PatientResponse;
import com.softwareA.patient.dto.response.PatientsResponse;
import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.model.Patient;
import com.softwareA.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PatientController {
    private final PatientService patientService;


    // FIND PATIENT BY NAME, CITIZEN ID, HEALTH INSURANCE NUM, database ID, phoneNumber
    @GetMapping
    public ApiResponse<PatientsResponse> getAllPatients() {
        //TODO: add filter condition here
        List<Patient> patients = patientService.getAllPatients();
        PatientsResponse response = PatientsResponse.builder().limit(10).page(1).total(10).patients(patients).build();
        return ApiResponse.<PatientsResponse>builder().result(response).build();
    }

    @GetMapping(path = "/{patientId}")
    public ApiResponse<PatientResponse> getPatient(@PathVariable("patientId") String patientId) {
        UUID uuid;
        try {
            uuid = UUID.fromString(patientId);

        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_UUID); // Define an appropriate error code
        }
        Optional<Patient> patient = patientService.getPatientById(uuid);
        if (patient.isEmpty()) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND); // Define an appropriate error code
        }
        PatientResponse response = PatientResponse.builder().patient(patient.get()).build();
        return ApiResponse.<PatientResponse>builder().result(response).build();
    }


    // UPDATE PATIENT INFO

    //

}
