package com.softwareA.hospital.service;

import com.softwareA.hospital.payment.BillingService;
import com.softwareA.hospital.payment.BillingServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalService {
    private final BillingServiceFactory billingServiceFactory;

    @Autowired
    public HospitalService(BillingServiceFactory billingServiceFactory) {
        this.billingServiceFactory = billingServiceFactory;
    }

    public String processHospitalPayment(String option, double amount) {
        BillingService billingService = billingServiceFactory.getBillingService(option);
        return billingService.processPayment(amount);
    }
}
