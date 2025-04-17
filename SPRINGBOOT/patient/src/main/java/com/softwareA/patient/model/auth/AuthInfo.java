package com.softwareA.patient.model.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthInfo {
    String userId;
    String userRole;
}
