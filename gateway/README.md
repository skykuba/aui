# Gateway

Spring Cloud Gateway module for the AUI system. It routes incoming API requests to backend services and applies restricted CORS for local frontends.

## What It Does

- Exposes a single entry point on port `8080`.
- Proxies client endpoints to the client service.
- Proxies invoice endpoints to the invoice service.
- Applies global CORS configuration for browser clients with explicit allowed origins.

## Tech Stack

- Java `17`
- Spring Boot `3.5.6`
- Spring Cloud Gateway (`spring-cloud-starter-gateway-server-webflux`)
- Maven Wrapper (`./mvnw`)

## Routes

Configured in `src/main/resources/application.yml`:

| Route ID | Incoming Path | Upstream (default) | Env Variable |
|---|---|---|---|
| `client_route` | `/api/clients/**` | `http://localhost:8081` | `CLIENT_SERVICE_URL` |
| `invoice_route` | `/api/invoices/**` | `http://localhost:8082` | `INVOICE_SERVICE_URL` |

The gateway reads upstream URLs from environment variables with the defaults shown above.

## Configuration

Main files:

- `src/main/resources/application.yml` - routes + CORS
- `src/main/resources/application.properties` - app name + gateway debug logging

Default server port:

- `server.port=8080`

Environment variables used by this module:

- `CLIENT_SERVICE_URL` (default: `http://localhost:8081`)
- `INVOICE_SERVICE_URL` (default: `http://localhost:8082`)
- `SPRING_PROFILES_ACTIVE` (set by Dockerfiles to `prod` or `dev`)

Current CORS policy (`spring.cloud.gateway.globalcors`):

- Allowed origins:
  - `http://localhost:4200`
  - `http://localhost:8090`
- Allowed methods: `GET`, `POST`, `PUT`, `DELETE`, `PATCH`, `OPTIONS`
- Allowed headers: `*`

## Run Locally (Maven)

From `gateway/`:

```bash
./mvnw spring-boot:run
```

Optional explicit upstreams:

```bash
CLIENT_SERVICE_URL=http://localhost:8081 INVOICE_SERVICE_URL=http://localhost:8082 ./mvnw spring-boot:run
```

Build JAR:

```bash
./mvnw clean package
```

## Run With Docker (Gateway Only)

From `gateway/`:

```bash
docker build -t aui-gateway:local .
docker run --rm -p 8080:8080 \
  -e CLIENT_SERVICE_URL=http://host.docker.internal:8081 \
  -e INVOICE_SERVICE_URL=http://host.docker.internal:8082 \
  aui-gateway:local
```

## Test

From `gateway/`:

```bash
./mvnw test
```

Current test suite includes basic Spring context startup validation in `src/test/java/org/example/gateway/GatewayApplicationTests.java`.

## Quick API Checks

Use `requests.http` (JetBrains HTTP client) to run sample requests through the gateway.

- Base URL: `http://localhost:8080`
- Includes sample client and invoice CRUD request sequences.
