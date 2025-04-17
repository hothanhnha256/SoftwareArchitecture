package com.softwareA.patient.controller;

import com.softwareA.patient.dto.request.MedicalOrderItemRequestDTO;
import com.softwareA.patient.dto.response.ApiResponse;
import com.softwareA.patient.model.MedicalOrderItem;
import com.softwareA.patient.service.MedicalOrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medical-order-items")
public class MedicalOrderItemController {
    private final MedicalOrderItemService medicalOrderItemService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<MedicalOrderItem>>> getAll(MedicalOrderItemRequestDTO dto,
                                                                      @PageableDefault(size = 20, page = 0) Pageable pageable) {
        //TODO authorization
        Page<MedicalOrderItem> page = this.medicalOrderItemService.getAllMedicalOrderItems(dto, pageable);
        return ResponseEntity.ok().body(ApiResponse.<List<MedicalOrderItem>>builder()
                .limit(page.getSize())
                .page(page.getNumber())
                .result(page.getContent())
                .total(page.getTotalPages())
                .build());
    }

    // FOR OTHER SERVICE
    @PostMapping("/by-ids")
    public ResponseEntity<ApiResponse<List<MedicalOrderItem>>> getAllByIds(List<String> ids) {
        List<MedicalOrderItem> list = this.medicalOrderItemService.getAllMedicalOrderItemsByIds(ids);
        return ResponseEntity.ok().body(ApiResponse.<List<MedicalOrderItem>>builder()
                .result(list)
                .build());
    }
}
