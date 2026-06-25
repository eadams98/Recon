package com.gateway.recon.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@Component
public class GatewayRequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(GatewayRequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        long start = System.currentTimeMillis();

        String method = exchange.getRequest().getMethod().toString();
        URI originalUri = exchange.getRequest().getURI();

        log.info("[GW:{}] IN  method={} uri={}", requestId, method, originalUri);

        return chain.filter(exchange)
                .doOnError(ex -> {
                    log.warn("[GW:{}] ERROR type={} message={}",
                            requestId,
                            ex.getClass().getSimpleName(),
                            ex.getMessage());
                })
                .doFinally(signalType -> {
                    long durationMs = System.currentTimeMillis() - start;

                    Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
                    URI routedUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
                    Object status = exchange.getResponse().getStatusCode();

                    String routeId = route != null ? route.getId() : "NO_ROUTE";

                    log.info("[GW:{}] OUT route={} routedUri={} status={} durationMs={} signal={}",
                            requestId,
                            routeId,
                            routedUri,
                            status,
                            durationMs,
                            signalType);
                });
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}