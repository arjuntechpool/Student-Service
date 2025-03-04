package com.techpool.apigateway.config;

import com.techpool.apigateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("student-service", r -> r.path("/api/students/**")
                        .filters(f -> f
                                .filter(filter.apply(new JwtAuthenticationFilter.Config()))
                                .stripPrefix(1))
                        .uri("lb://student-service"))
                .route("student-details-service", r -> r.path("/api/details/**")
                        .filters(f -> f
                                .filter(filter.apply(new JwtAuthenticationFilter.Config()))
                                .stripPrefix(1))
                        .uri("lb://student-details-service"))
                .build();
    }
}