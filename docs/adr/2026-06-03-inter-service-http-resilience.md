# ADR: Inter-service HTTP resilience (report-service)

**Status:** Accepted  
**Date:** 2026-06-03  
**Context:** `recon-report-service` is the only service that calls peer services over HTTP (`user-service` for JWT/relationship verification, `email-service` for report-created notifications).

## Problem

Outbound calls used a default `RestTemplate` with no connect/read timeouts and no retry. A slow or briefly unavailable peer could block threads indefinitely or fail on the first transient network blip, which is weak for a portfolio-grade microservices story.

## Decision

1. Configure one shared `@LoadBalanced` `RestTemplate` with explicit connect and read timeouts via `RestTemplateBuilder`.
2. Route calls through `InterServiceHttpClient` backed by a programmatic `RetryTemplate` (not `@Retryable`).
3. Externalize timeout and retry settings under `recon.inter-service.http.*` in `application.properties`.

## Retried failures (GET / idempotent reads only)

- `ResourceAccessException` (connection refused, I/O errors, timeouts surfaced by the client)
- `SocketTimeoutException` (when thrown as a retryable cause)
- `HttpServerErrorException` (5xx responses from the peer)

Up to **3 total attempts** (configurable) with **fixed backoff** defaulting to **300ms** between attempts.

## Not retried

- **4xx** (`HttpClientErrorException`) — not listed in `retryOn()`; includes auth (401/403), validation (400), and business-rule responses propagated from `user-service`
- **POST `/email/report-created`** — side-effecting (sends email); **single attempt only** to avoid duplicate notifications without idempotency keys
- Non-HTTP failures that are not in the retry policy

## Out of scope

- Circuit breakers, bulkheads, rate limiting
- Idempotency keys for email
- Migrating to `WebClient` or Resilience4j
- Retrying POST or other mutating calls
- Changes to `recon-email-service`’s unused `RestTemplate` bean
- Eureka/registry health-based routing beyond existing `@LoadBalanced` behavior

## Consequences

- GET verification calls are more tolerant of brief outages and 5xx from core.
- Email notification failures surface immediately; operators may need to retry at the business layer or re-trigger manually.
- Slightly higher tail latency when retries occur (bounded by attempt count and backoff).
