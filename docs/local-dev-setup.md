# Recon local development

## Service map

| Service | Container | Host URL | Purpose |
|---------|-----------|----------|---------|
| MySQL | `mysql` | internal `:3306` | Seeded `recon` database |
| Eureka | `eureka` | http://localhost:8761 | Service discovery |
| Core (user-service) | `recon-core` | http://localhost:4000 | Auth, profile, connections |
| Reports | `recon-reports` | http://localhost:4001 | Weekly reports API |
| Email | `recon-email` | http://localhost:4003 | SES email service |
| React client (optional) | `recon-client` | http://localhost:3000 | CRA dev server via fullstack overlay |

## Quick start

**Backend only:**
```bash
docker compose up --build
```

**Full stack (requires MERN `recon-client` at `RECON_CLIENT_CONTEXT`):**
```bash
docker compose -f docker-compose.yaml -f docker-compose.fullstack.yml up --build
```

**Stop:**
```bash
docker compose down
# or with fullstack:
docker compose -f docker-compose.yaml -f docker-compose.fullstack.yml down
```

**Reset database (re-run seed scripts):**

MySQL init scripts run only when the `recon-mysql-data` volume is first created. To wipe and re-seed:

```bash
docker compose down -v
docker compose up --build
# fullstack:
docker compose -f docker-compose.yaml -f docker-compose.fullstack.yml down -v
docker compose -f docker-compose.yaml -f docker-compose.fullstack.yml up --build
```

## Seeded test users

Password for all: `password`

| Role | Email |
|------|-------|
| Contractor | `contractor.1@yahoo.com` |
| Trainee | `trainee.1@yahoo.com` |
| School | `school.1@yahoo.com` |

## Smoke tests

**Public endpoint:**
```bash
curl http://localhost:4000/user/test
```

**Login (contractor):**
```bash
curl -s -H "Content-Type: application/json" \
  -d '{"username":"contractor.1@yahoo.com","password":"password"}' \
  http://localhost:4000/user/authenticate/contractor
```

**Actuator health:**
```bash
curl http://localhost:4000/actuator/health
curl http://localhost:4001/actuator/health
curl http://localhost:8761/actuator/health
```

**Frontend (Playwright, from MERN repo):**
```bash
cd Recon/recon-client
PLAYWRIGHT_SKIP_WEB_SERVER=1 PLAYWRIGHT_BASE_URL=http://localhost:3000 \
  npx playwright test e2e/docker-backend-smoke.spec.js
```

## Environment files

| File | Purpose |
|------|---------|
| `.env` | Host port mappings, `RECON_CLIENT_CONTEXT` |
| `env/recon-core.env` | Core service ports + AWS placeholders for local Docker |
| `env/recon-report.env` | Report service |
| `env/recon-client.env` | CRA env for fullstack overlay |

## Known local-dev gaps

- `/bucket/picture` returns 400 without real S3 — UI falls back to default avatar
- Report month/week dropdowns stay empty until a year is selected (cascade UX)
- Report service returns 500 (not 401) when `Authorization` header is missing
