package com.gateway.recon.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Configuration
public class RouteLocatorConfig {

    private static final String CONNECT_TIMEOUT_ATTR = "connect-timeout";
    private static final String RESPONSE_TIMEOUT_ATTR = "response-timeout";

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("http://httpbin.org:80"))
                .route(p -> p
                        .host("*.circuitbreaker.com")
                        .filters(f -> f.circuitBreaker(config -> config.setName("mycmd")))
                        .uri("http://httpbin.org:80"))
                .route("recon-core-ms-retry", p -> p
                        .path("/api/recon/core/user/test/flaky/**")
                        .filters(f ->
                                f.stripPrefix(3)
                                    .retry(rc -> rc
                                            .setRetries(3)
                                            .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)
                                            .setMethods(HttpMethod.GET)
                                            .setBackoff(Duration.ofMillis(100), Duration.ofSeconds(1), 2, false)
                                    )
                        )
                        .metadata(CONNECT_TIMEOUT_ATTR, 10000) // overrides global set at propertise level
                        .metadata(RESPONSE_TIMEOUT_ATTR, 5000) // overrides global set at propertise level
                        .uri("lb://USER-SERVICE")

                )
                .route("recon-core-ms", p -> p
                        .path("/api/recon/core/**")
                        .filters(f ->
                                f.stripPrefix(3)
                                .circuitBreaker(config ->
                                        config
                                            .setName("recon-core-ms")
                                            .addStatusCode("INTERNAL_SERVER_ERROR")
                                            .setFallbackUri("forward:/fallback")
                                )
                        )
                        .metadata(CONNECT_TIMEOUT_ATTR, 10000) // overrides global set at propertise level
                        .metadata(RESPONSE_TIMEOUT_ATTR, 5000) // overrides global set at propertise level
                        .uri("lb://USER-SERVICE")

                )
                .route(p -> p
                        .path("/gets")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("http://httpbin.org:80"))
                .build();
    }
}
