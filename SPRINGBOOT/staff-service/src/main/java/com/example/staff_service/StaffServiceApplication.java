package com.example.staff_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StaffServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StaffServiceApplication.class, args);
	}

}
