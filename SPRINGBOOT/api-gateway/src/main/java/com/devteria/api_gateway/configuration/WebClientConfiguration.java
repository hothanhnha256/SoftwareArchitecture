package com.devteria.api_gateway.configuration;

import com.devteria.api_gateway.repository.IdentityClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;


@Configuration
public class WebClientConfiguration {

    private static final Logger log = LoggerFactory.getLogger(WebClientConfiguration.class);
    @Value("${identity.base-url}")
    String identityBaseUrl;

    @Bean
    @Primary
    WebClient webClient(){
        return WebClient.builder()
                .baseUrl(identityBaseUrl)
                .build();
    }

    @Bean
    @Qualifier("fallbackWebClient")
    public WebClient fallbackWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8888") // assuming fallback is local
                .build();
    }

    @Bean
    CorsWebFilter corsWebFilter(){
        log.info("corsWebFilter");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5500"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    IdentityClient identityClient(WebClient webClient){
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient)).build();

        return httpServiceProxyFactory.createClient(IdentityClient.class);
    }

}