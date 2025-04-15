package com.softwareA.hospital.payment;

import org.springframework.stereotype.Service;

@Service
public class MasterCardBillingService implements BillingService {
    @Override
    public String processPayment(double amount) {
        System.out.println("Processing payment via Credit Card: $" + amount);
        return "Master Card payment of $" + amount + " processed successfully.";
    }
}