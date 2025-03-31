package com.softwareA.patient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "schedule-b", url = "http://localhost:8082") //TODO: check name and url
public interface ScheduleClient {
}
