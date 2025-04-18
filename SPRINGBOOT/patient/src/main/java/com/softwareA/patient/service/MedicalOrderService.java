package com.softwareA.patient.service;

import com.softwareA.patient.client.InventoryClient;
import com.softwareA.patient.client.StaffClient;
import com.softwareA.patient.dto.Staff;
import com.softwareA.patient.dto.request.CreateMedicalOrderDTO;
import com.softwareA.patient.dto.request.MedicalOrderItemDTO;
import com.softwareA.patient.dto.response.MedicalOrderResponse;
import com.softwareA.patient.dto.response.PatientGeneralDTO;
import com.softwareA.patient.exception.AppException;
import com.softwareA.patient.exception.ErrorCode;
import com.softwareA.patient.mapper.MedicalOrderMapper;
import com.softwareA.patient.mapper.PatientMapper;
import com.softwareA.patient.model.MedicalOrder;
import com.softwareA.patient.model.MedicalOrderItem;
import com.softwareA.patient.model.MedicalOrder_OrderItem;
import com.softwareA.patient.model.auth.AuthInfo;
import com.softwareA.patient.repository.MedicalOrderRepository;
import com.softwareA.patient.repository.MedicalOrder_OrderItemRepository;
import com.softwareA.patient.repository.PatientRepository;
import com.softwareA.patient.service.validator.MedicalOrderItemValidator;
import com.softwareA.patient.service.validator.PatientValidator;
import feign.FeignException;
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
    private final PatientRepository patientRepository;
    private final MedicalOrderItemValidator medicalOrderItemValidator;
    private final MedicalOrderMapper medicalOrderMapper;
    private final StaffClient staffClient;
    private final PatientMapper patientMapper;

    @Transactional
    public MedicalOrder createMedicalOrder(CreateMedicalOrderDTO dto, AuthInfo authInfo) {
        // FOR DOCTOR
        if (!authInfo.getUserRole().equals("DOCTOR")) {
            throw new AppException(ErrorCode.FORBIDDEN, "You are not authorized to create a medical order");
        }
        // CHECK IF PATIENT EXISTS
        if (patientValidator.patientExists(dto.getPatientId())) {
            throw new AppException(ErrorCode.PATIENT_NOT_FOUND, "Patient not found");
        }
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

    public MedicalOrderResponse getMedicalOrderById(String id, AuthInfo authInfo) {
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
        // GET MEDICAL ORDER ITEMS INFO FROM INVENTORY SERVICE
        integrateMedicalItemInfo(order_orderItems);
        MedicalOrderResponse response = medicalOrderMapper.toMedicalOrderResponse(order);
        response.setMedicalOrderItems(order_orderItems); // add the order items to the response
        // get patient info
        patientRepository.findById(order.getPatientId()).ifPresent(patient -> {
            PatientGeneralDTO patientDto = patientMapper.toGeneralDTO(patient);  // Map the patient to DTO
            response.setPatient(patientDto);  // Set the DTO to the response
        });
        // add doctor info
        try {
            Staff doctor = staffClient.getDoctorById(order.getDoctorId()).getResult();
            response.setDoctor(doctor);
        } catch (FeignException ex) {
            log.error("Error while fetching doctor info: {}", ex.getMessage(), ex);
        }
        return response;
    }

    private void integrateMedicalItemInfo(List<MedicalOrder_OrderItem> orderItems) {
        List<String> medicalOrderItemIds = orderItems.stream()
                .map(MedicalOrder_OrderItem::getMedicalOrderItemId)
                .collect(Collectors.toList());
        try {
            List<MedicalOrderItem> medicalOrderItems = inventoryClient.getAllMedicalOrderItemsByIds(medicalOrderItemIds).getResult();
            Map<String, MedicalOrderItem> itemMap = medicalOrderItems.stream()
                    .collect(Collectors.toMap(MedicalOrderItem::getCode, item -> item));
            for (MedicalOrder_OrderItem orderItem : orderItems) {
                MedicalOrderItem matchingItem = itemMap.get(orderItem.getMedicalOrderItemId());
                orderItem.setMedicalOrderItem(matchingItem);
            }
        } catch (FeignException feignException) {
            log.error("Error while fetching doctor info: {}", feignException.getMessage(), feignException);
        }

    }
}
