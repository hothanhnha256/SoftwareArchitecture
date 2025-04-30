package com.devteria.identity.feignclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Request;

@Configuration
public class FeignRedirectConfig {
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(5000, 5000); // Timeout settings
    }
}
