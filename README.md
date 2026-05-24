# Recon

Java backend implementation of Recon Project.

Frontend (React) lives in the MERN repo: `../MERN/Recon/recon-client` (path configurable via `RECON_CLIENT_CONTEXT` in `.env`).

## Docker — backend only

```bash
docker compose up --build
```

| Service | URL |
|---------|-----|
| Core (auth, profile) | http://localhost:4000 |
| Reports | http://localhost:4001 |
| Email | http://localhost:4003 |
| Eureka | http://localhost:8761 |

## Docker — full stack (backend + React dev server)

Requires the client repo checked out at `RECON_CLIENT_CONTEXT` (default: `../MERN/Recon/recon-client`).

```bash
docker compose -f docker-compose.yaml -f docker-compose.fullstack.yml up --build
```

Open http://localhost:3000 — the browser calls APIs on `localhost:4000` / `localhost:4001` (host-mapped backend ports).

Stop everything:

```bash
docker compose -f docker-compose.yaml -f docker-compose.fullstack.yml down
```
