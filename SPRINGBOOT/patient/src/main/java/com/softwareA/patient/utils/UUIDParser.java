package com.softwareA.patient.utils;


import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;

import java.util.UUID;

public class UUIDParser {
    public static UUID parseUUID(String uuidStr) {
        try {
            return UUID.fromString(uuidStr);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_UUID);
        }
    }
}
