package com.gateway.recon.configs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class RouteLocatorConfigTest {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void definesReconCoreRoutes() {
        List<Route> routes = routeLocator.getRoutes().collectList().block();

        assertThat(routes)
                .extracting(Route::getId)
                .contains("recon-core-ms", "recon-core-ms-retry");
    }

    @Test
    void reconCoreRouteTargetsUserService() {
        List<Route> routes = routeLocator.getRoutes().collectList().block();

        Route reconCoreRoute = routes.stream()
                .filter(route -> "recon-core-ms".equals(route.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(reconCoreRoute.getUri().toString()).isEqualTo("lb://USER-SERVICE");
    }
}
