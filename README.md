# Contract Wisor API

Backend service for **Contract Wisor**, a contract management and analysis
platform. It ingests documents from multiple sources, runs long-lived parsing
and AI-analysis pipelines as durable workflows, and exposes a secured REST API
for managing documents, users, and analysis results.

> This repository is published for **portfolio and demonstration** purposes.
> See [LICENSE](LICENSE) — all rights reserved.

---

## Highlights

- **Durable document pipelines** — parsing and AI analysis run as
  [Temporal](https://temporal.io/) workflows, so long-running jobs survive
  restarts and retry deterministically.
- **Multi-format ingestion** — PDF, Office documents, and scanned images, with
  OCR fallback (Tesseract) for image-only content.
- **Stateless JWT security** — access/refresh token auth with role-based access
  control over endpoints and menus.
- **Scheduled batch framework** — cron-driven jobs with retry, locking, and
  timeout handling, resolved dynamically by service name.
- **Remote sources** — pull documents over FTP/SFTP.

## Features

| Area | Description |
|------|-------------|
| Authentication | JWT access + refresh tokens, password reset, account lockout |
| Authorization | Roles, menus, and per-endpoint API-URL permissions (RBAC) |
| Documents | Upload, metadata, types, relations, favorites, views, statistics |
| Analysis | AI-driven contract Q&A / clause analysis via an external AI service |
| Workflows | Temporal-based parse and analyze pipelines with monitoring |
| Batch | Cron-scheduled jobs with retry/lock/timeout management |
| Integrations | FTP/SFTP ingestion, SMTP email notifications |

## Tech stack

- **Java 17**, **Spring Boot 3.4** (Web, Data JPA, Security, Mail)
- **PostgreSQL** + Hibernate (schema validated, not auto-generated)
- **Temporal** (`temporal-sdk`) for workflow orchestration
- **JJWT** for token handling, **JSch** for SFTP
- **Apache PDFBox / POI / Tika** and **Tess4j (Tesseract)** for document parsing & OCR
- **Maven** build, **Docker** multi-stage image

## Architecture

```
controller   REST endpoints
   │
service      business logic ──► workflow   Temporal workflows + activities
   │                                │
repository   Spring Data JPA        └──► external AI / parse services
   │
PostgreSQL
```

Cross-cutting: `config` (security, Temporal, async, scheduling), `batch`
(cron framework), `scheduler` (workflow & endpoint monitoring), `specification`
(JPA dynamic queries), `util` (JWT, logging, filters).

## Getting started

### Prerequisites
- JDK 17+
- PostgreSQL
- A running Temporal cluster
- (Optional) the companion AI/parse service for analysis features

### Configuration
All runtime settings are externalized via environment variables. The committed
`application.properties` contains **safe localhost defaults only — no secrets**.
Copy the template and fill in real values:

```bash
cp .env.example .env
# edit .env, then export the variables (or load them into your runtime)
```

Key variables (see [`.env.example`](.env.example) for the full list):

| Variable | Purpose |
|----------|---------|
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | PostgreSQL connection |
| `JWT_SECRET`, `JWT_REFRESH_SECRET` | Base64 token signing keys (generate fresh) |
| `SFTP_SECRET` | SFTP integration secret |
| `TEMPORAL_TARGET` | Temporal frontend address |
| `CWAI_API_TARGET`, `PARSE_DOCUMENT_API` | External AI / parse services |
| `MAIL_HOST`, `MAIL_PORT` | SMTP server |

Generate signing keys, for example:
```bash
openssl rand -base64 48
```

### Run locally
```bash
./mvnw spring-boot:run
```
The API starts on `http://localhost:8080`.

### Build & run with Docker
```bash
docker build -t contract-wisor-api .
docker run -p 8081:8081 --env-file .env contract-wisor-api
```

## Project structure
```
src/main/java/ai/niksar/contract_wisor_api/
├── config/         Spring, security, Temporal, async & scheduler config
├── controller/     REST controllers
├── service/        Business logic
├── workflow/       Temporal workflows & activities (parse / analyze)
├── batch/          Cron batch framework (retry, lock, timeout)
├── scheduler/      Workflow & endpoint monitoring jobs
├── repository/     Spring Data JPA repositories
├── specification/  Dynamic JPA query specifications
├── model/  dto/  helpers/  util/  exception/
```

## License

Proprietary — © 2026 Niksar. All rights reserved. See [LICENSE](LICENSE).
