# Trip Expense Splitter Backend

A robust REST API application for managing shared expenses during group trips. Easily track who paid what and simplify the settling up process among travel companions.

## 📋 Project Description

Trip Expense Splitter Backend is a **REST API application built on Spring Boot 4.0** with **Clean Architecture** design principles. This is the backend component that powers efficient expense management for group trips.

> **Note:** This repository contains only the backend. The mobile app is maintained in a separate repository: [Money Manager Mobile App](https://github.com/s1zyy/money_manager) (Flutter + Dart)

### Core Features

- ✅ **User Authentication** — Secure JWT-based authentication
- ✅ **Trip Management** — Create, update, and archive trips with multiple participants
- ✅ **Expense Tracking** — Record shared expenses with automatic split calculation
- ✅ **Participant Management** — Add friends to trips via unique join codes
- ✅ **Balance Calculation** — Automatic calculation of who owes whom
- ✅ **Daily Limits** — Track spending against budgets
- ✅ **Clean Architecture** — Layered design with clear separation of concerns

## 🛠 Technology Stack

- **Java 21** — Modern programming language with latest features
- **Spring Boot 4.0.2** — Production-ready application framework
- **Spring Data JPA** — ORM for database interaction
- **Spring Security** — Authentication and authorization framework
- **JWT (jjwt 0.12.5)** — Secure token-based authentication
- **PostgreSQL 15** — Primary production database
- **H2 Database** — Embedded database for development/testing
- **Flyway** — Database versioning and migration management
- **Lombok** — Reduce boilerplate code with annotations
- **Maven 3.8+** — Dependency and build management

## 📁 Project Architecture

This project follows **Clean Architecture** principles, organizing code into distinct layers with well-defined responsibilities:

```
src/main/java/vlad/corp/money_manager_backend/
├── application/              # Application Layer - Use Cases & Business Logic
│   ├── auth/                # Authentication use cases (login, register)
│   ├── trip/                # Trip management use cases
│   ├── expense/             # Expense management use cases
│   ├── participant/         # Participant management use cases
│   ├── calculator/          # Balance & limit calculation logic
│   ├── exception/           # Application-level exceptions
│   └── port/                # Interfaces for external services
│
├── domain/                  # Domain Layer - Core Business Rules
│   ├── model/              # Domain entities (Trip, Expense, Participant)
│   ├── value_objects/      # Money and other value objects
│   ├── policy/             # Business policies (TripAccessPolicy)
│   ├── repository/         # Repository interfaces (data abstraction)
│   ├── exceptions/         # Domain-specific exceptions
│   └── service/            # Domain services (optional)
│
├── infrastructure/          # Infrastructure Layer - Technical Details
│   ├── config/             # Configuration classes (UseCase wiring)
│   ├── persistence/        # JPA repository implementations
│   │   ├── trip/          # Trip persistence
│   │   ├── expense/       # Expense persistence
│   │   └── participant/   # Participant persistence
│   └── security/          # Security implementation (JWT, filters)
│
└── presentation/           # Presentation Layer - API Endpoints
    ├── controller/        # REST controllers
    │   ├── AuthController
    │   ├── TripController
    │   ├── ExpenseController
    │   └── ParticipantController
    ├── dto/               # Request/Response DTOs
    ├── mapper/            # DTO ↔ Domain Model converters
    └── ApiExceptionHandler.java  # Global exception handling
```

### Clean Architecture Advantages

- **Testability** — Business logic is independent of frameworks
- **Maintainability** — Clear responsibilities for each layer
- **Flexibility** — Easy to modify or swap implementations
- **Independence** — Core logic doesn't depend on external libraries
- **Scalability** — Simple to add new features without breaking existing code

## 🚀 Quick Start

### Prerequisites

- **Java 21** or higher
- **Maven 3.8+** (or use the included `mvnw` wrapper)
- **Docker & Docker Compose** (recommended for database)
- **Git**

### Installation & Setup

1. **Clone the repository:**
```bash
git clone <repository-url>
cd money_manager_backend
```

2. **Configure environment (optional):**
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```
The default configuration is pre-set for local development with Docker.

3. **Start PostgreSQL with Docker:**
```bash
docker-compose up -d
```
This will start PostgreSQL on `localhost:5432` with the credentials configured in `application.properties`.

Verify the database is running:
```bash
docker-compose ps
```

4. **Build the project:**
```bash
./mvnw clean install
```

5. **Run the application:**
```bash
./mvnw spring-boot:run
```

The API will be available at: **`http://localhost:8080`**

### Database Initialization

The database schema is automatically created on first startup using Flyway migrations. Check `src/main/resources/db/migration/` for migration scripts.

## 📚 API Documentation

All endpoints are RESTful and return JSON responses. Authentication is required for most endpoints using JWT tokens.

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/register` | Register a new participant account |
| `POST` | `/api/auth/login` | Login and receive JWT token |

### Trip Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/trips` | Create a new trip |
| `GET` | `/api/trips` | List all trips for current user |
| `GET` | `/api/trips/{tripId}` | Get trip details |
| `PUT` | `/api/trips/{tripId}` | Update trip information |
| `POST` | `/api/trips/{tripId}/archive` | Archive a trip (owner only) |
| `POST` | `/api/trips/{tripId}/join` | Join a trip using join code |
| `POST` | `/api/trips/{tripId}/leave` | Leave a trip |
| `GET` | `/api/trips/{tripId}/dashboard` | Get trip dashboard with balances |

### Expense Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/trips/{tripId}/expenses` | Create a new expense |
| `GET` | `/api/trips/{tripId}/expenses` | List all expenses in a trip |
| `GET` | `/api/trips/{tripId}/expenses/{expenseId}` | Get expense details |
| `PUT` | `/api/trips/{tripId}/expenses/{expenseId}` | Update expense |
| `DELETE` | `/api/trips/{tripId}/expenses/{expenseId}` | Delete expense |

### Participant Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/trips/{tripId}/participants` | List all trip participants |

## 📊 Database Schema

### Tables Overview

- **participants** — User accounts (name, email, password hash)
- **trips** — Trip records (name, dates, budget, owner, status)
- **trip_participants** — Many-to-many relationship between trips and participants
- **expenses** — Trip expenses (amount, payer, date, description)
- **expense_participants** — Many-to-many relationship for expense splits

### Key Relationships

```
Participants (1) ──► (M) Trips (as owner)
       │
       └──► (M) Trips (as participant via trip_participants)
            │
            └──► (M) Expenses (as payer)
                 │
                 └──► (M) Expense_Participants (split among participants)
```

See `src/main/resources/db/migration/V1_innit_tables_create.sql` for the complete schema.

## 🔐 Security

### JWT Authentication
- All API endpoints are protected with JWT tokens (except `/api/auth/register` and `/api/auth/login`)
- Secret key is stored securely in `application.properties`

### Data Validation
- Input data is validated at the controller level using Spring Validation annotations
- Custom exceptions are handled by a global exception handler (`ApiExceptionHandler`)
- Domain-level validation ensures business rules are enforced

### Password Security
- Passwords are hashed using Spring Security's built-in password encoders
- Never stored in plain text

## 📝 Usage Examples

### 1. Register a New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3. Create a Trip

```bash
curl -X POST http://localhost:8080/api/trips \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Summer Vacation 2024",
    "startDate": "2024-06-01",
    "endDate": "2024-06-15",
    "totalBudget": 5000,
    "prepaidExpenses": 1500
  }'
```

### 4. Join a Trip

```bash
curl -X POST http://localhost:8080/api/trips/join
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json"
    -d '{
        "code": "ABC123"
    }'
```

### 5. Add an Expense

```bash
curl -X POST http://localhost:8080/api/trips/{tripId}/expenses \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 120.50,
    "description": "Restaurant dinner",
    "date": "2024-06-05",
    "participantIds": [
      "550e8400-e29b-41d4-a716-446655440000",
      "550e8400-e29b-41d4-a716-446655440001"
    ]
  }'
```

### 6. Get Trip Dashboard (Balances)

```bash
curl -X GET http://localhost:8080/api/trips/{tripId}/dashboard \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```


## 🐳 Docker

### Running with Docker Compose

```bash
docker-compose up -d
```

This starts a PostgreSQL 15 container on port 5432.

### Stopping

```bash
docker-compose down
```

## 🔗 Related Projects

This is the **backend** component of the Money Manager system. Other related repositories:

- **[Money Manager Mobile App](https://github.com/s1zyy/money_manager)** — Flutter + Dart mobile application for iOS and Android

### System Architecture

```
┌──────────────────────────┐
│  Money Manager Mobile    │
│  App (Flutter + Dart)    │
│  (iOS/Android)           │
└───────────┬──────────────┘
            │
            │ HTTP/REST API
            │
┌───────────▼──────────────┐
│   Money Manager Backend   │
│   (Spring Boot - This)    │◄─── You are here
│   (Java 21)              │
└───────────┬──────────────┘
            │
            │ JDBC
            │
┌───────────▼──────────────┐
│   PostgreSQL Database    │
│   (Primary Storage)      │
└──────────────────────────┘
```

## 📞 Developer Support

If you have questions or issues with the code:

1. Check the application logs
2. Make sure all dependencies are installed: `./mvnw dependency:resolve`
3. Create an issue in the repository with a description of the problem

## 📄 License

The project is distributed under the **MIT License**. See the [LICENSE](LICENSE) file for more details.

This means you can:
- ✅ Use the software for any purpose
- ✅ Copy, modify, and distribute the software
- ✅ Include the software in proprietary applications

Just make sure to include a copy of the license and copyright notice.

---

**Last Updated:** February 2026

