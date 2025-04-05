package com.softwareA.patient.payment;

import org.springframework.stereotype.Service;

@Service
public class CreditCardBillingService implements BillingService {
    @Override
    public String processPayment(double amount) {
        System.out.println("Processing payment via Credit Card: $" + amount);
        return "Credit Card payment of $" + amount + " processed successfully.";
    }
}