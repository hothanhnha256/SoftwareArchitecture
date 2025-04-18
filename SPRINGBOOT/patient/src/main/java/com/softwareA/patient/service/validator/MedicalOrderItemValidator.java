package com.softwareA.patient.service.validator;

import com.softwareA.patient.client.InventoryClient;
import com.softwareA.patient.dto.request.MedicalOrderItemDTO;
import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.model.MedicalOrderItem;
import com.softwareA.patient.model.MedicalOrder_OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class MedicalOrderItemValidator {
    private final InventoryClient inventoryClient;

    public void checkIfMedicalOrderItemsExist(List<MedicalOrderItemDTO> orderItems) {
        List<MedicalOrderItem> list = inventoryClient.getAllMedicalOrderItemsByIds(
                        orderItems.stream()
                                .map(MedicalOrderItemDTO::getMedicalOrderId)
                                .toList())
                .getResult();
        // store existing ids in a set for faster lookup
        Set<String> existingIds = list.stream()
                .map(MedicalOrderItem::getCode)
                .collect(Collectors.toSet());

        // compare with original request list
        for (MedicalOrderItemDTO item : orderItems) {
            if (!existingIds.contains(item.getMedicalOrderId()))
            {
                throw new AppException(ErrorCode.MEDICAL_ORDER_ITEM_NOT_FOUND, "Medical order item " + item.getMedicalOrderId() + " not found");
            }
        }
    }

}
