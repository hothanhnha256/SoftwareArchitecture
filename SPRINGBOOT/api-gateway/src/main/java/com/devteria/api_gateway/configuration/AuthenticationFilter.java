package com.devteria.api_gateway.configuration;

import com.devteria.api_gateway.service.IdentityService;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {

    IdentityService identityService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Authentication Filter");

        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) {
            return unAuthenticated(exchange.getResponse());
        }
        String token = authHeader.getFirst().replace("Bearer ", "");
        log.info("token: {}", token);

        //verify token
        var test=identityService.introspect(token).subscribe(
                introspectResponseApiResponse -> {
                    log.info("introspectResponseApiResponse: {}", introspectResponseApiResponse);
                }
        );
        log.info("test: {}", test);


        //delegate identity service
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    Mono<Void> unAuthenticated(ServerHttpResponse response) {
        String body="unAuthenticated";
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(Mono.just(
                response.bufferFactory().wrap(body.getBytes())
        ));
    }
}
