package com.softwareA.patient.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BillingServiceFactory {
    private final Map<String, BillingService> billingStrategies;

    @Autowired
    public BillingServiceFactory(List<BillingService> billingServices) {
        this.billingStrategies = new HashMap<>();
        for (BillingService service : billingServices) {
            billingStrategies.put(service.getClass().getSimpleName(), service);
        }
    }

    public BillingService getBillingService(String option) {
        return billingStrategies.getOrDefault(option, billingStrategies.get("CashBillingService"));
    }
}
