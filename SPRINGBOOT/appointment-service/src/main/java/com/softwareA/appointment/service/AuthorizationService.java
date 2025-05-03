package com.softwareA.appointment.service;

import com.softwareA.appointment.exception.AppException;
import com.softwareA.appointment.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    public void authorizeCreateAppointment(String userRole) {
        if (!userRole.equals("USER")) {
            throw new AppException(ErrorCode.FORBIDDEN, "Only user are allowed to create an appointment");
        }
    }
}
