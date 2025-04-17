package com.devteria.api_gateway.configuration;

import com.devteria.api_gateway.dto.response.ApiResponse;
import com.devteria.api_gateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;
    @Qualifier("fallbackWebClient")
    WebClient fallbackClient;

    @NonFinal
    private final Map<String, Set<String>> publicEndpoints = new HashMap<>() {
        {
            put("/identity/auth/.*", Set.of("GET", "POST")); // Both GET and POST allowed
            put("/identity/users", Set.of("GET")); // Only GET is allowed
            put("/patient/", Set.of("POST")); // Only POST is allowed
            put("/appointment/test-patient-feign", Set.of("GET")); // Only GET is allowed
            put("/appointment/departments", Set.of("GET")); // Only GET is allowed
            put("/identity/users/create-patient", Set.of("POST")); // Only GET is allowed
            put("/identity/users/staff-account", Set.of("POST")); //TODO: temp
        }
    };

    @Value("${app.prefix-api}")
    @NonFinal
    private String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter....");
        // return default chain if the request is a public endpoint
        if (isPublicEndpoint(exchange.getRequest()))
        {
            log.info("public endpoint");
            return chain.filter(exchange);
        }

        // Get token from authorization header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (CollectionUtils.isEmpty(authHeader))
            return unauthenticated(exchange.getResponse());

        String token = authHeader.getFirst().replace("Bearer ", "");
        log.info("Token: {}", token);

        return identityService.introspect(token).flatMap(introspectResponse -> {
            log.info(introspectResponse.toString());
            if (introspectResponse.getResult().isValid()) {
                log.info(introspectResponse.getResult().toString());
                return identityService.myinfo(token).flatMap(userResponse -> { // truyền token vào myinfo()
                    log.info("UserId " + userResponse.getResult().getId().toString());
                    log.info("UserRole " + userResponse.getResult().getRole().toString());
                    if (userResponse.getResult() != null) {
                        ServerHttpRequest mutatedRequest = exchange.getRequest()
                                .mutate()
                                .header("UserId", userResponse.getResult().getId().toString()) // ID của User
                                .header("UserRole", userResponse.getResult().getRole().toString()) // Truyền lại token
                                .build();

                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    } else {
                        return unauthenticated(exchange.getResponse());
                    }
                });
            } else {
                return unauthenticated(exchange.getResponse());
            }
        }).onErrorResume(throwable -> {
            log.error("Error during authentication: here");
            return serviceUnavailable(exchange.getResponse());
        });
    }

    @Override
    public int getOrder() {
        return -1;
    }

    // private boolean isPublicEndpoint(ServerHttpRequest request) {
    //     return Arrays.stream(publicEndpoints)
    //             .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    // }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        String method = request.getMethod().toString(); // Get HTTP method (e.g., GET, POST)

        return publicEndpoints.entrySet().stream()
                .anyMatch(entry -> path.matches(apiPrefix + entry.getKey()) && entry.getValue().contains(method));
    }

    Mono<Void> serviceUnavailable(ServerHttpResponse response) {
        ApiResponse<Void> fallbackResponse = ApiResponse.<Void>builder()
                .message("Identity service is currently unavailable. Please try again later.")
                .build();
        // Return the fallback response as a Mono to write it to the response body
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        String body = null;
        try {
            body = objectMapper.writeValueAsString(fallbackResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(401)
                .message("Unauthenticated")
                .build();
        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}