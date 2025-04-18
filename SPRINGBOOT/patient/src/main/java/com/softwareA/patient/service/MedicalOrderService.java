package com.softwareA.patient.service;

import com.softwareA.patient.client.InventoryClient;
import com.softwareA.patient.dto.request.CreateMedicalOrderDTO;
import com.softwareA.patient.dto.request.MedicalOrderItemDTO;
import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.model.MedicalOrder;
import com.softwareA.patient.model.MedicalOrderItem;
import com.softwareA.patient.model.MedicalOrder_OrderItem;
import com.softwareA.patient.model.auth.AuthInfo;
import com.softwareA.patient.repository.MedicalOrderRepository;
import com.softwareA.patient.repository.MedicalOrder_OrderItemRepository;
import com.softwareA.patient.repository.PatientRepository;
import com.softwareA.patient.service.validator.MedicalOrderItemValidator;
import com.softwareA.patient.service.validator.PatientValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalOrderService {
    private final MedicalOrderRepository medicalOrderRepository;
    private final MedicalOrder_OrderItemRepository medicalOrder_orderItemRepository;
    private final InventoryClient inventoryClient;
    private final PatientValidator patientValidator;
    private final MedicalOrderItemValidator medicalOrderItemValidator;

    @Transactional
    public MedicalOrder createMedicalOrder(CreateMedicalOrderDTO dto, AuthInfo authInfo) {
        // FOR DOCTOR
        if (!authInfo.getUserRole().equals("DOCTOR")) {
            throw new AppException(ErrorCode.FORBIDDEN, "You are not authorized to create a medical order");
        }
        // CHECK IF PATIENT EXISTS
//        if (patientValidator.patientExists(dto.getPatientId())) {
//            throw new AppException(ErrorCode.PATIENT_NOT_FOUND, "Patient not found");
//        }
        List<MedicalOrder_OrderItem> items = new ArrayList<>();
        String medicalOrderId = UUID.randomUUID().toString();
        MedicalOrder medicalOrder = new MedicalOrder();
        medicalOrder.setId(medicalOrderId);
        medicalOrder.setPatientId(dto.getPatientId());
        medicalOrder.setDoctorId(authInfo.getUserId());
        // CHECK IF MEDICAL ITEMS EXIST
        try {
            medicalOrderItemValidator.checkIfMedicalOrderItemsExist(dto.getOrderItems());
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("here");
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Medical order item not found");
        }

        for (MedicalOrderItemDTO item : dto.getOrderItems()) {
            MedicalOrder_OrderItem orderItem = new MedicalOrder_OrderItem();
            orderItem.setMedicalOrderId(medicalOrderId);
            orderItem.setMedicalOrderItemId(item.getMedicalOrderId());
            orderItem.setQuantity(item.getQuantity());
            items.add(orderItem);
        }
        medicalOrder_orderItemRepository.saveAll(items);
        return medicalOrderRepository.save(medicalOrder);
    }

    public MedicalOrder getMedicalOrderById(String id, AuthInfo authInfo) {
        // FOR DOCTOR, PATIENT
        if (!authInfo.getUserRole().equals("DOCTOR") && !authInfo.getUserRole().equals("USER")) {
            throw new AppException(ErrorCode.FORBIDDEN, "You are not authorized to view this medical order");
        }
        Optional<MedicalOrder> medicalOrderOptional = medicalOrderRepository.findById(id);
        if (medicalOrderOptional.isEmpty()) {
            throw new AppException(ErrorCode.MEDICAL_ORDER_ITEM_NOT_FOUND, "Medical order not found");
        }
        MedicalOrder order = medicalOrderOptional.get();
        // GET MEDICAL ORDER ITEMS MAPPING OF THIS ORDER
        List<MedicalOrder_OrderItem> order_orderItems = medicalOrder_orderItemRepository.findByMedicalOrderId(id);
        try {
            // GET MEDICAL ORDER ITEMS INFO FROM INVENTORY SERVICE
            List<MedicalOrderItem> orderItems = inventoryClient.getAllMedicalOrderItemsByIds(
                            order_orderItems
                                    .stream()
                                    .map(MedicalOrder_OrderItem::getMedicalOrderItemId)
                                    .collect(Collectors.toList()))
                    .getResult();
            Map<String, MedicalOrderItem> itemMap = orderItems.stream()
                    .collect(Collectors.toMap(MedicalOrderItem::getCode, item -> item));
            // assign medicalOrderItem to orderItem
            for (MedicalOrder_OrderItem orderItem : order_orderItems) {
                MedicalOrderItem matchingItem = itemMap.get(orderItem.getMedicalOrderItemId());
                orderItem.setMedicalOrderItem(matchingItem);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return order;
    }
}
