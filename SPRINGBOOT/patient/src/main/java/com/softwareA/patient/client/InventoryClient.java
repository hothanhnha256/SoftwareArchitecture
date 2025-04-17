package com.softwareA.patient.client;

import com.softwareA.patient.dto.response.ApiResponse;
import com.softwareA.patient.model.MedicalOrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "inventory-service", url = "${inventory-service.url}")
public interface InventoryClient {
    @PostMapping("/medical-order-items/by-ids")
    ApiResponse<List<MedicalOrderItem>> getAllMedicalOrderItemsByIds(List<String> ids);
}
