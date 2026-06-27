<p align="center">
  <h1 align="center">💸 Trip Expense Splitter — Backend</h1>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-15-316192?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>
  <img src="https://img.shields.io/badge/License-MIT-22c55e?style=for-the-badge"/>
</p>

<p align="center">
  <b><a href="https://github.com/s1zyy/money_manager">📱 Mobile App (Flutter)</a></b> •
  <b><a href="#-api-reference">API Reference</a></b> •
  <b><a href="#-quick-start">Quick Start</a></b>
</p>

---

## About

Backend for **Money Manager** — a Splitwise-like mobile app for group trips. Handles user auth, trip lifecycle, expense tracking, and automatic balance calculation (who owes whom and how much).

Built with Clean Architecture — domain logic is fully decoupled from frameworks and infrastructure.

---

## Features

| | Feature |
|---|---|
| 🔐 | JWT-based authentication (register / login) |
| ✈️ | Full trip lifecycle — create, update, archive, delete |
| 👥 | Join trips via unique invite codes |
| 💰 | Expense tracking with flexible participant splits |
| 📊 | Automatic balance calculation per participant |
| 📉 | Daily limit & budget tracking |
| 🗄️ | Schema versioning with Flyway migrations |
| 🐳 | One-command PostgreSQL setup via Docker Compose |

---

## Architecture

Clean Architecture with four strict layers — no shortcuts between them:

```
src/main/java/vlad/corp/money_manager_backend/
│
├── domain/              ← Core: entities, repository interfaces, policies
│   ├── model/           # Trip, Expense, Participant
│   ├── value_objects/   # Money
│   ├── policy/          # TripAccessPolicy — who can do what
│   └── repository/      # Interfaces (not implementations)
│
├── application/         ← Use cases (one class = one operation)
│   ├── auth/            # Login, Register
│   ├── trip/            # CreateTrip, UpdateTrip, ArchiveTrip, JoinTrip…
│   ├── expense/         # AddExpense, UpdateExpense, DeleteExpense…
│   └── calculator/      # Balance calculation, daily limits
│
├── infrastructure/      ← Technical details: JPA, security, config
│   ├── config/          # UseCase wiring (@Configuration, not @Service)
│   ├── persistence/     # Spring Data JPA repositories
│   └── security/        # JWT filter, Spring Security config
│
└── presentation/        ← REST layer: controllers, DTOs, mappers
    ├── controller/      # AuthController, TripController, ExpenseController
    ├── dto/             # Request / Response objects
    ├── mapper/          # DTO ↔ Domain converters
    └── ApiExceptionHandler.java
```

**Key decisions:**
- Use cases are plain Java classes wired via `@Configuration` beans — not annotated with `@Service`. This keeps domain/application layers free of Spring.
- `TripAccessPolicy` enforces ownership/participation rules at the domain level, before any infrastructure is touched.
- Flyway handles all schema changes — no manual SQL, no `ddl-auto: create`.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.2 |
| Security | Spring Security + JWT (jjwt 0.12.5) |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL 15 |
| Migrations | Flyway |
| Build | Maven (mvnw wrapper included) |
| Dev DB | Docker Compose |

---

## Quick Start

**Prerequisites:** Java 21, Docker

```bash
# 1. Clone
git clone https://github.com/s1zyy/money_manager_backend.git
cd money_manager_backend

# 2. Start PostgreSQL
docker compose up -d

# 3. Run
./mvnw spring-boot:run
```

API is live at **`http://localhost:8080`**. Database schema is auto-applied by Flyway on first start.

```bash
# Run tests
./mvnw test

# Build JAR
./mvnw clean package -DskipTests
```

---

## API Reference

All endpoints are under `/api`. JWT token required in `Authorization: Bearer <token>` header (except auth endpoints).

### Auth

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login, get JWT token |

### Trips

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/trips` | Create a trip |
| `GET` | `/api/trips` | List user's trips |
| `GET` | `/api/trips/{id}` | Get trip details |
| `PUT` | `/api/trips/{id}` | Update trip |
| `DELETE` | `/api/trips/{id}` | Delete trip |
| `POST` | `/api/trips/{id}/archive` | Archive trip (owner only) |
| `POST` | `/api/trips/{id}/join` | Join trip by invite code |
| `POST` | `/api/trips/{id}/leave` | Leave trip |
| `GET` | `/api/trips/{id}/dashboard` | Get balances & spending stats |

### Expenses

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/trips/{tripId}/expenses` | Add expense |
| `GET` | `/api/trips/{tripId}/expenses` | List expenses |
| `GET` | `/api/trips/{tripId}/expenses/{id}` | Get expense |
| `PUT` | `/api/trips/{tripId}/expenses/{id}` | Update expense |
| `DELETE` | `/api/trips/{tripId}/expenses/{id}` | Delete expense |

### Participants

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/trips/{tripId}/participants` | List trip participants |

### Example Requests

<details>
<summary>Register & Login</summary>

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email": "john@example.com", "password": "secret123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "john@example.com", "password": "secret123"}'
# → {"token": "eyJhbGci..."}
```

</details>

<details>
<summary>Create a trip & add an expense</summary>

```bash
TOKEN="eyJhbGci..."

# Create trip
curl -X POST http://localhost:8080/api/trips \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Barcelona 2025",
    "startDate": "2025-07-01",
    "endDate": "2025-07-10",
    "totalBudget": 3000
  }'

# Add expense
curl -X POST http://localhost:8080/api/trips/{tripId}/expenses \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 84.50,
    "description": "Tapas dinner",
    "date": "2025-07-03",
    "participantIds": ["uuid-1", "uuid-2"]
  }'
```

</details>

<details>
<summary>Join a trip by invite code</summary>

```bash
curl -X POST http://localhost:8080/api/trips/{tripId}/join \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"code": "ABC123"}'
```

</details>

---

## Database Schema

```
participants ──(1:M)──► trips (as owner)
     │
     └──(M:M via trip_participants)──► trips (as member)
                                            │
                                            └──(1:M)──► expenses
                                                            │
                                                            └──(M:M via expense_participants)
```

Tables: `participants`, `trips`, `trip_participants`, `expenses`, `expense_participants`, `trip_statuses`

Full schema: [`db/migration/V1_innit_tables_create.sql`](src/main/resources/db/migration/V1_innit_tables_create.sql)

---

## Related

**[📱 Money Manager Mobile App](https://github.com/s1zyy/money_manager)** — Flutter client for iOS & Android

```
┌─────────────────────────┐
│  Flutter App (iOS/Android)│
└────────────┬────────────┘
             │ HTTP / REST
┌────────────▼────────────┐
│  Spring Boot (this repo) │ ◄── you are here
└────────────┬────────────┘
             │ JDBC
┌────────────▼────────────┐
│      PostgreSQL 15       │
└─────────────────────────┘
```

---

## License

MIT — see [LICENSE](LICENSE)
