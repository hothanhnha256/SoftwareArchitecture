package com.softwareA.patient.controller;

import com.softwareA.patient.dto.request.CreateEmergencyContactDTO;
import com.softwareA.patient.dto.request.UpdateEmergencyContactDTO;
import com.softwareA.patient.dto.response.ApiResponse;
import com.softwareA.patient.model.EmergencyContact;
import com.softwareA.patient.service.EmergencyContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emergency-contacts")
@RequiredArgsConstructor
@Slf4j
public class EmergencyContactController {
    private final EmergencyContactService emergencyContactService;

    // CREATE EMERGENCY CONTACT (for patients themselves)
    @PostMapping(path = "")
    public ResponseEntity<ApiResponse<EmergencyContact>> addEmergencyContact(
            @RequestHeader("UserId") String userId,
            @RequestHeader("UserRole") String role,
            @RequestBody CreateEmergencyContactDTO dto) {
        log.info("addEmergencyContact: " + dto.toString());
        EmergencyContact createdContact = this.emergencyContactService.createEmergencyContact(userId, role, dto);
        return ResponseEntity.status(201)
                .body(ApiResponse.<EmergencyContact>builder()
                        .result(createdContact).build());
    }

    @DeleteMapping(path = "/{contactId}")
    public ResponseEntity<ApiResponse<Void>> deleteEmergencyContact(@RequestHeader("UserId") String userId,
                                                                    @RequestHeader("UserRole") String role,
                                                                    @PathVariable("contactId") String contactId) {
        // get id from jwt token
        log.info("deleteEmergencyContact: " + contactId);
        this.emergencyContactService.deleteEmergencyContact(userId, role, contactId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{contactId}")
    public ResponseEntity<ApiResponse<EmergencyContact>> updateEmergencyContact(
            @RequestHeader("UserId") String userId,
            @RequestHeader("UserRole") String role,
            @PathVariable("contactId") String contactId,
            @RequestBody UpdateEmergencyContactDTO dto) {
        // get id from jwt token
        log.info("updateEmergencyContact: " + dto.toString());
        EmergencyContact updatedEmergencyContact = this.emergencyContactService.updateEmergencyContact(userId, role, contactId, dto);
        return ResponseEntity.ok()
                .body(ApiResponse.<EmergencyContact>builder().result(updatedEmergencyContact).build());
    }
}
