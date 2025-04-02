package com.devteria.api_gateway.service;


import com.devteria.api_gateway.dto.ApiResponse;
import com.devteria.api_gateway.dto.request.IntrospectRequest;
import com.devteria.api_gateway.dto.response.IntrospectResponse;
import com.devteria.api_gateway.dto.response.UserResponse;
import com.devteria.api_gateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;
    private final WebClient webClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token){
        return identityClient.introspect(IntrospectRequest.builder()
                .token(token)
                .build());
    }

    public Mono<ApiResponse<UserResponse>> myinfo(String token) {
        // Thêm header xác thực vào request
        return webClient.get()
                .uri("/users/my-info")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<UserResponse>>() {});
    }
}