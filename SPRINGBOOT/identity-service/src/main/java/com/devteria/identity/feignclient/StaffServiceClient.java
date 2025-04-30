package com.devteria.identity.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.devteria.identity.dto.request.CreateStaffDTO;
import com.devteria.identity.dto.response.ApiResponse;

@FeignClient(name = "staff-service", url = "${feignclient.staff-service.url}")
public interface StaffServiceClient {
    @PostMapping("/create")
    ApiResponse<CreateStaffDTO> createStaffProfile(@RequestBody CreateStaffDTO dto);
}
