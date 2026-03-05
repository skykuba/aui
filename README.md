# AUI - Accounts Office System

Monorepo for a small Accounts Office application with a web frontend, API gateway, and two backend services (clients and invoices).

## Project Modules

- `frontend` - Angular UI
- `gateway` - Spring Cloud Gateway, single API entry point
- `accounts-office-client` - client service
- `accounts-office-invoice` - invoice service
- `init-db.sql` - PostgreSQL initialization script used by Docker Compose

## Architecture (Runtime)

- Frontend calls Gateway: `http://localhost:8090 -> http://gateway:8080/api`
- Gateway routes:
  - `/api/clients/**` -> client service
  - `/api/invoices/**` -> invoice service
- Both backend services use PostgreSQL (`postgres:13`)

## Run Modes

### 1) Default local stack (build from source)

```bash
docker compose up --build
```

Main URLs:
- Frontend: `http://localhost:8090`
- Gateway API: `http://localhost:8080`

### 2) Development stack (hot reload oriented)

Uses `Dockerfile.dev` and source mounts.

```bash
docker compose -f docker-compose.dev.yml up --build
```

Main URLs:
- Frontend (dev server): `http://localhost:4200`
- Gateway API: `http://localhost:8080`

### 3) Production-like stack (prebuilt images)

Uses images from GHCR.

```bash
docker compose -f docker-compose.prod.yml up -d
```

Image publish workflow: `.github/workflows/publish-docker-images.yml`.

## Service Ports (Compose)

- `frontend` -> `8090:80` (default/prod)
- `frontend-dev` -> `4200:4200` (dev)
- `gateway` / `gateway-dev` -> `8080:8080`
- `accounts-office-client` -> internal `8081`
- `accounts-office-invoice` -> internal `8082`
- `postgres` -> `5432:5432`

## Useful Commands

Start stack:

```bash
docker compose up --build
```

Stop stack:

```bash
docker compose down
```

Stop stack and remove volumes (resets DB data):

```bash
docker compose down -v
```

## Module Documentation

- `frontend/README.md`
- `gateway/README.md`
- `accounts-office-client/README.md`
- `accounts-office-invoice/README.md`

