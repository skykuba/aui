# Frontend

Angular UI for the Accounts Office system.

## What this app does

The frontend lets you:
- manage clients (list, create, edit, delete),
- browse client invoices,
- create and edit invoices,
- open invoice details.

## Run locally

```bash
npm install
ng serve
```

Default local URL:
- `http://localhost:4200`

## Run with Docker (dev stack)

```bash
docker compose -f docker-compose.dev.yml up --build
```

Frontend is exposed on:
- `http://localhost:4200`

## Useful scripts

```bash
ng serve
ng build
ng test
```
