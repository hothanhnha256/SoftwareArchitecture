package com.devteria.api_gateway.configuration;

import com.devteria.api_gateway.dto.ApiResponse;
import com.devteria.api_gateway.dto.response.UserResponse;
import com.devteria.api_gateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;

    @NonFinal
    private String[] publicEndpoints = {
            "/identity/auth/.*",
            "/identity/users"
    };

    @Value("${app.prefix-api}")
    @NonFinal
    private String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter....");

        if (isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);

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
                log.info("hjere");
                return identityService.myinfo(token).flatMap(userResponse -> { // truyền token vào myinfo()
                    log.info("here man");
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
            log.error(throwable.getMessage());
            return unauthenticated(exchange.getResponse());
        });
    }


    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isPublicEndpoint(ServerHttpRequest request){
        return Arrays.stream(publicEndpoints)
                .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    }

    Mono<Void> unauthenticated(ServerHttpResponse response){
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1401)
                .message("Unauthenticated")
                .build();

        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}