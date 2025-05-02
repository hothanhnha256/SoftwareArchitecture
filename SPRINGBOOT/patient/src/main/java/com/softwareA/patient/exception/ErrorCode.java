package com.softwareA.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_UUID(2000, "Invalid UUID", HttpStatus.BAD_REQUEST),
    MEDICAL_ORDER_ITEM_NOT_FOUND(4004, "Medical order item not found", HttpStatus.NOT_FOUND),
    RESOURCE_NOT_FOUND(4005, "Resource not found", HttpStatus.NOT_FOUND),
    PATIENT_NOT_FOUND(3000, "Patient not found", HttpStatus.NOT_FOUND),

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),

    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),

    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),

    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),

    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),

    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),

    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    FORBIDDEN(1009, "Forbidden", HttpStatus.FORBIDDEN),
    MEDICAL_ORDER_NOT_FOUND(1010, "Medical order not found", HttpStatus.NOT_FOUND),
    PRESCRIPTION_EXISTS(1011, "Prescription already exists for this medical order", HttpStatus.CONFLICT),
    MEDICATION_NOT_FOUND(1012, "Medication not found", HttpStatus.NOT_FOUND),
    PRESCRIPTION_NOT_FOUND(1013, "Prescription not found", HttpStatus.NOT_FOUND),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
