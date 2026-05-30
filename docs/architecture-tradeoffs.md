# Architecture Tradeoffs

This document describes how Recon is structured today and where it intentionally stops short of production-grade distributed-system design. Recon is a **learning and portfolio project**: it demonstrates microservice boundaries, service discovery, JWT auth, and Docker-based local development—not a hardened production deployment.

## Current Project Scope

Recon is a training-reporting application for **contractors**, **trainees**, and **schools**:

- Contractors submit weekly reports for linked trainees.
- Schools read **finalized** reports for trainees on their roster.
- Trainees can add a **retort** after a report is finalized.

This repository contains the **Java/Spring Boot** backend services and Docker Compose stack. The **React** client lives in a sibling repository (`recon-client`) and can be wired in via a Compose overlay.

The project prioritizes **local developer experience**: one command to bring up MySQL, Eureka, core, reports, and email services, with seeded test users and health-gated startup order.

## Current Architecture

### Services

| Module | Compose service | Port (host) | Role |
|--------|-----------------|-------------|------|
| `recon-discovery-service` | `eureka` | 8761 | Netflix Eureka service registry |
| `reconV2` | `recon-core` | 4000 | Auth, profiles, contractor/trainee/school APIs, JWT issuance, S3 profile routes |
| `recon-report-service` | `recon-reports` | 4001 | Weekly reports, finalize, retort, school portal reads |
| `recon-email-service` | `recon-email` | 4003 | Outbound email (Amazon SES) |

### Data and communication

- **MySQL 8** — single `recon` database shared by **core** and **reports** (email does not use a datasource). Schema and seed data are applied on first Docker volume create via init SQL scripts.
- **Eureka** — JVM services register at startup; Compose `depends_on` with healthchecks orders startup after MySQL and Eureka are ready (email waits on MySQL for startup ordering only).
- **Inter-service HTTP** — the report service forwards the caller’s JWT to core (`user-service`) over **RestTemplate** for identity and relationship checks, rather than duplicating that logic locally.
- **Frontend** — React app calls **two API origins**: core (`:4000`) for auth/profile and reports (`:4001`) for report CRUD.

### Local runtime

Docker Compose in this repo is the primary way to run the full backend stack. An optional `docker-compose.fullstack.yml` overlay adds the React dev server when `recon-client` is checked out at `RECON_CLIENT_CONTEXT`.

## Known Tradeoffs

These are deliberate simplifications for local development and portfolio clarity—not oversights presented as finished production design.

### Shared Database

Core and reports connect to the same MySQL instance and `recon` schema (`spring.datasource.url=.../${MYSQL_SERVICE_NAME}:3306/recon`). Email is part of the Compose stack but does not persist to this database.

**Why:** One database container, one init path, and straightforward seed data make the stack easy to run and explain.

**Cost:** Services are not isolated at the persistence layer. Schema coupling, migration ordering, and connection pool contention would need careful handling in a real multi-team deployment. A production path would typically use **database-per-service** (or at least schema-per-service with strict ownership).

### Limited Resilience / No Retry Layer

Report operations depend on synchronous HTTP calls to core for identity and relationship checks. There is no circuit breaker, retry policy, or outbox/event-driven fallback in this codebase.

**Why:** Keeps the call graph easy to trace for learning and debugging.

**Cost:** Core latency or downtime directly affects report APIs. Transient failures are not retried; cascading failures are possible. Production systems would add resilience patterns (retries with backoff, circuit breakers, bulkheads, and often async messaging for non-critical paths like email).

### Local Development Secrets / Config

Several values are suitable for local Docker only:

- MySQL credentials (`root` / `root` in service `application.properties`; `env/mysql.env` for the container).
- Placeholder AWS env vars (`AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`) in `env/recon-core.env` and `env/recon-email.env`; core and email read these via `System.getenv`, not Spring `application.properties` placeholders.
- JWT signing secret externalized via `JWT_SECRET` with a **local-dev default** in `application.properties` (must be replaced in any real deployment).
- Short JWT expiry windows in properties (`jwtExpirationMs`, `jwtRefreshExpirationMs`) tuned for testing, not production session policy.

**Why:** Reduces friction when cloning the repo and running `docker compose up`.

**Cost:** Easy to mistake local defaults for secure configuration. Nothing in this repo should be deployed as-is without secrets management (e.g. AWS Secrets Manager, Parameter Store, or a vault) and environment-specific config.

### Docker Compose Scope

Compose files orchestrate **local development and demos**, not production:

- No TLS termination, ingress controller, or WAF.
- MySQL is not published to the host by default (`RECON_MYSQL_HOST_PORT=0`).
- Healthchecks gate startup order but do not implement rolling updates or autoscaling.
- Images are built from Dockerfiles that run `spring-boot:run` via Maven wrapper—convenient for dev, not optimized production image builds.

**Why:** Compose matches the goal of a reproducible local stack for portfolio reviewers and contributors.

**Cost:** Production would require a different runtime (e.g. ECS/EKS, Kubernetes), CI-built JAR/container artifacts, managed RDS, and observability (metrics, tracing, centralized logging).

## Next Production Hardening Improvements

Reasonable follow-ups **without** re-architecting the whole system today:

1. **Secrets and config** — inject all secrets from a secrets manager; remove hardcoded DB credentials from `application.properties`; use Spring profiles (`local`, `staging`, `prod`).
2. **JWT policy** — strong random `JWT_SECRET` per environment; production-appropriate token and refresh lifetimes; consistent 401 responses on auth failures (report service currently returns 500 when `Authorization` is missing).
3. **Database boundaries** — plan schema-per-service or separate databases; owned migrations per service.
4. **Resilience** — Resilience4j (or similar) on core calls from report service; idempotent report writes where applicable.
5. **Observability** — structured logging, correlation IDs across services, metrics and alerting on health and error rates.
6. **CI/CD and images** — multi-stage Docker builds producing runnable JARs; automated tests (integration tests already exist for report retort visibility); pipeline gates before deploy.
7. **AWS integrations** — real S3/SES configuration with IAM roles instead of placeholder keys; `/bucket/picture` currently needs real credentials for full local behavior.
8. **Security review** — rate limiting on auth endpoints, CORS policy per environment, dependency scanning, and actuator endpoint exposure locked down in prod.

None of the above is implemented end-to-end in this repository today. The [README](../README.md) and [local-dev-setup.md](local-dev-setup.md) describe what **is** validated locally (health checks, smoke tests, selected integration tests).
