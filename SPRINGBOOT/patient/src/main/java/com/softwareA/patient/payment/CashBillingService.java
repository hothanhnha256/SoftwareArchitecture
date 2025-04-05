package com.softwareA.patient.payment;

import org.springframework.stereotype.Service;

@Service
public class CashBillingService implements BillingService {
    @Override
    public String processPayment(double amount) {
        System.out.println("Processing payment via Cash: $" + amount);
        return "Cash payment of $" + amount + " processed successfully.";
    }
}