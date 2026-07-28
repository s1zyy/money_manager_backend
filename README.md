<p align="center">
  <h1 align="center">✈️ TripPace — Backend</h1>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21"/>
  <img src="https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot 4.0"/>
  <img src="https://img.shields.io/badge/PostgreSQL-15-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL 15"/>
  <img src="https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT Auth"/>
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker Compose"/>
  <img src="https://img.shields.io/badge/Deployed-Railway-0B0D0E?style=for-the-badge&logo=railway&logoColor=white" alt="Railway"/>
  <img src="https://img.shields.io/badge/License-MIT-22c55e?style=for-the-badge" alt="License MIT"/>
</p>

<p align="center">
  <b><a href="https://github.com/s1zyy/money_manager">📱 Mobile App (Flutter)</a></b> •
  <b><a href="#api-reference">API Reference</a></b> •
  <b><a href="#quick-start">Quick Start</a></b>
</p>

---

## About

Backend for **TripPace** — a mobile app for splitting trip expenses between friends. Handles user auth, trip lifecycle, expense tracking, automatic balance calculation, and email invites for virtual participants.

Built with Clean Architecture — domain logic is fully decoupled from frameworks and infrastructure. Deployed on Railway at `https://trippace.up.railway.app`.

---

## Features

| | Feature |
|---|---|
| 🔐 | JWT-based authentication (register / login) |
| ✈️ | Full trip lifecycle — create, update, archive, unarchive, delete |
| 👥 | Join trips via unique invite codes; virtual participants for non-app members |
| 📧 | Email invites for virtual participants via Brevo HTTP API |
| 🔗 | Claim flow — virtual participant links their real account via invite token |
| 💰 | Expense tracking with equal or custom splits per participant |
| 📊 | Automatic balance calculation & settlement suggestions |
| 📉 | Per-participant budget & daily limit tracking |
| 🛡️ | App version enforcement via `X-App-Version` header |
| 🗑️ | Soft delete & full account deletion |
| 🗄️ | Schema versioning with Flyway migrations (V1–V10) |
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
| `PUT` | `/api/trips/{id}` | Update trip name / dates / currency |
| `DELETE` | `/api/trips/{id}` | Delete trip (owner only) |
| `POST` | `/api/trips/{id}/archive` | Archive trip (owner only) |
| `POST` | `/api/trips/{id}/unarchive` | Unarchive trip (owner only) |
| `POST` | `/api/trips/join` | Join trip by invite code |
| `GET` | `/api/trips/{id}/dashboard` | Get balances, spending stats & expenses |
| `GET` | `/api/trips/{id}/settlement` | Get settlement transfers (who pays whom) |

### Participants & Invites

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/trips/{tripId}/participants` | List trip participants |
| `POST` | `/api/trips/{tripId}/leave` | Leave trip |
| `DELETE` | `/api/trips/{tripId}/participants/{pid}` | Remove participant (owner only) |
| `POST` | `/api/trips/{tripId}/participants/virtual` | Add virtual participant (owner only) |
| `PUT` | `/api/trips/{id}/my-budget` | Update your own budget |
| `PUT` | `/api/trips/{id}/participants/{pid}/budget` | Update any participant's budget (owner only) |
| `POST` | `/api/trips/{tripId}/participants/{pid}/invite` | Send email invite to virtual participant |
| `POST` | `/api/invites/check` | Check invite token (returns trip & participant info) |
| `POST` | `/api/invites/claim` | Claim invite — register and join as real participant |
| `POST` | `/api/invites/claim-with-login` | Claim invite — login with existing account |

### Account

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/participants/me` | Get current user profile |
| `PUT` | `/api/participants/me` | Update profile (name) |
| `PUT` | `/api/participants/me/password` | Change password |
| `DELETE` | `/api/participants/me` | Soft delete account (preserves trip data) |
| `DELETE` | `/api/participants/me/full` | Full delete account + all owned trips |

### Expenses

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/trips/{tripId}/expenses` | Add expense |
| `GET` | `/api/trips/{tripId}/expenses` | List expenses |
| `GET` | `/api/trips/{tripId}/expenses/{id}` | Get expense |
| `PUT` | `/api/trips/{tripId}/expenses/{id}` | Update expense |
| `DELETE` | `/api/trips/{tripId}/expenses/{id}` | Delete expense |

### Example Requests

<details>
<summary>Register & Login</summary>

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email": "john@example.com", "password": "secret123", "name": "John"}'

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
    "budget": 3000,
    "startDate": "2025-07-01",
    "endDate": "2025-07-10",
    "currency": "EUR"
  }'

# Add expense (equal split)
curl -X POST http://localhost:8080/api/trips/{tripId}/expenses \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 84.50,
    "description": "Tapas dinner",
    "date": "2025-07-03",
    "splitMode": "EQUAL",
    "payerId": "uuid-of-payer",
    "participantIds": ["uuid-1", "uuid-2"]
  }'
```

</details>

<details>
<summary>Join a trip by invite code</summary>

```bash
curl -X POST http://localhost:8080/api/trips/join \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"joinCode": "ABC12345", "budget": 1500}'
```

</details>

<details>
<summary>Dashboard response shape</summary>

`GET /api/trips/{id}/dashboard` returns:

```json
{
  "trip": {
    "id": "uuid",
    "name": "Barcelona 2025",
    "startDate": "2025-07-01",
    "endDate": "2025-07-10",
    "currency": "EUR",
    "status": "ACTIVE",
    "joinCode": "ABC12345",
    "participantBudgets": { "uuid-1": 3000, "uuid-2": 1500 }
  },
  "myStats": {
    "participantId": "uuid-1",
    "budget": 3000,
    "dailyLimit": 142.50,
    "spentToday": 42.25
  },
  "participants": [
    { "participantId": "uuid-1", "balance": 84.50 },
    { "participantId": "uuid-2", "balance": -84.50 }
  ],
  "expenseDtoList": [
    {
      "id": "uuid",
      "tripId": "uuid",
      "amount": 84.50,
      "payerId": "uuid-1",
      "splitMode": "EQUAL",
      "participantShares": { "uuid-1": null, "uuid-2": null },
      "date": "2025-07-03",
      "description": "Tapas dinner",
      "isPrepaid": false
    }
  ],
  "isOwner": true,
  "canLeave": false
}
```

`participantShares` value is `null` for `EQUAL` split (divide evenly) and an explicit amount for `CUSTOM` split.

</details>

---

## Database Schema

```
participants ──(1:M)──► trips (as owner)
     │
     └──(M:M via trip_participants)──► trips (as member, with per-participant budget)
                                            │
                                            └──(1:M)──► expenses
                                                            │
                                                            ├──(M:M via expense_participants — equal split)
                                                            └──(1:M via expense_custom_shares — custom split)
```

| Table | Description |
|-------|-------------|
| `participants` | Users and virtual participants (`is_virtual`, nullable email) |
| `trips` | Trip metadata, owner, dates, currency, join code |
| `trip_participants` | M:M join table with per-participant `budget` |
| `trip_statuses` | Enum-like lookup: UPCOMING / ACTIVE / ARCHIVED |
| `expenses` | Expense records with `split_mode` (EQUAL / CUSTOM) and `is_prepaid` flag |
| `expense_participants` | Participants included in an expense (EQUAL split — amount is null) |
| `expense_custom_shares` | Explicit share amounts per participant (CUSTOM split only) |

Migrations: [`db/migration/`](src/main/resources/db/migration/) — V1 initial schema through V10 (soft delete).

---

## Related

**[📱 TripPace Mobile App](https://github.com/s1zyy/money_manager)** — Flutter client for iOS & Android

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

Custom license — see [LICENSE](LICENSE)
