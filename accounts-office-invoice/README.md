# Accounts Office - Invoice Service

Simple Spring Boot service for managing invoices and lightweight client records in the Accounts Office system.

## Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Maven

## What This Service Does

- Creates, updates, lists, and deletes invoices
- Supports invoice queries by client, issuer, and payment status
- Stores simple client data used by invoice domain
- Handles client events (`client-created`, `client-updated`, `client-deleted`)

## Run Locally (Maven)

From `accounts-office-invoice`:

```bash
./mvnw spring-boot:run
```

## Important Environment Variables

- `SERVER_PORT` (default in compose: `8082`)
- `SPRING_DATASOURCE_URL` (example: `jdbc:postgresql://postgres:5432/invoice_db`)
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_JPA_HIBERNATE_DDL_AUTO`
