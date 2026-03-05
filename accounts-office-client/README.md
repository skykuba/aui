# Accounts Office - Client Service

Simple Spring Boot service for managing clients, addresses, and cities in the Accounts Office system.

## Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Maven

## What This Service Does

- Creates, updates, lists, and deletes clients
- Stores address and city data
- Communicates with the invoice service (via `INVOICE_SERVICE_URL`)

## Run Locally (Maven)

From `accounts-office-client`:

```bash
./mvnw spring-boot:run
```

## Important Environment Variables

- `SERVER_PORT` (default in compose: `8081`)
- `INVOICE_SERVICE_URL` (example: `http://accounts-office-invoice:8082`)
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`



