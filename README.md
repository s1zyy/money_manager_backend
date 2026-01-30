# Money Manager Backend

A full-featured server application for managing user finances. The system allows you to manage wallets, track transactions, and control financial flows.

## 📋 Project Description

Money Manager Backend is a **REST API application built on Spring Boot 4.0**. This is the **backend component** of the Money Manager system.

> **Note:** This repository contains only the backend. The mobile app is maintained in a separate repository: [Money Manager Mobile App](https://github.com/s1zyy/money_manager) (Flutter + Dart)

The backend provides functionality for:

- ✅ Managing user accounts with JWT authentication
- ✅ Creating and managing wallets
- ✅ Recording financial transactions (income/expenses)
- ✅ Viewing transaction history
- ✅ Validating data at the API level
- ✅ **Sharing wallets with friends** — add collaborators to manage finances together
- ✅ **Offline support** — modify and create transactions offline with automatic sync when internet connection is restored

## 🛠 Technology Stack

- **Java 21** — programming language
- **Spring Boot 4.0.2** — framework for building applications
- **Spring Data JPA** — ORM and database interaction
- **Spring Security** — authentication and authorization
- **JWT (jjwt)** — tokens for secure data transmission
- **PostgreSQL 15** — primary database
- **H2 Database** — embedded database for development
- **Flyway** — database migration management
- **Lombok** — reducing boilerplate code
- **Maven** — dependency management

## 🤝 Collaborative Features

### Shared Wallets
- **Add Friends** — invite other users to collaborate on a wallet
- **Shared Ownership** — multiple users can manage transactions in the same wallet
- **Real-time Synchronization** — all changes are instantly synced across all collaborators

### Offline Support
- **Offline Mode** — create and modify transactions without internet connection
- **Local Storage** — all changes are cached locally on the device
- **Automatic Sync** — when internet connection is restored, all pending transactions are automatically synchronized with the backend
- **Conflict Resolution** — intelligent handling of concurrent changes from multiple users
- **Status Tracking** — clear indication of synced vs pending transactions

## 📁 Project Architecture

This project follows **Clean Architecture** principles, ensuring clear separation of concerns and high maintainability. The architecture is organized into distinct layers:

```
src/main/java/vlad/corp/money_manager_backend/
├── application/          # Application Layer - Business logic and services
│   ├── auth/            # Authentication and authorization services
│   ├── exception/       # Custom exceptions
│   ├── transaction/     # Transaction handling logic
│   ├── user/            # User handling logic
│   └── wallet/          # Wallet handling logic
├── domain/              # Domain Layer - Core business logic
│   ├── model/           # Entities (User, Wallet, Transaction)
│   ├── repository/      # Repository interfaces (abstraction)
│   └── exception/       # Domain-specific exceptions
├── infrastructure/      # Infrastructure Layer - External services
│   └── ...             # Database implementation, configurations
└── presentation/        # Presentation Layer - REST API
    ├── controller/      # HTTP endpoints
    ├── dto/             # Data Transfer Objects
    ├── mapper/          # Converters between DTOs and domain models
    └── ApiExceptionHandler.java
```

### Clean Architecture Benefits

- **Independence from frameworks** — Core business logic is framework-agnostic
- **Testability** — Each layer can be tested independently
- **Maintainability** — Clear responsibilities for each layer
- **Flexibility** — Easy to swap implementations without affecting business logic
- **Scalability** — Simple to extend with new features

## 🚀 Quick Start

### Prerequisites

- Java 21 or higher
- Maven 3.8+
- Docker and Docker Compose (optional)
- PostgreSQL 15 (or use Docker Compose)

### Local Installation

1. **Clone the repository:**
```bash
git clone <repository-url>
cd money_manager_backend
```

2. **Configure environment variables:**
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```
Edit the database connection parameters if needed.

3. **Run PostgreSQL (with Docker):**
```bash
docker-compose up -d
```

4. **Build the project:**
```bash
./mvnw clean install
```

5. **Run the application:**
```bash
./mvnw spring-boot:run
```

The application will be available at: `http://localhost:8080`

## 📚 API Documentation

### Main Endpoints

#### Authentication
- `POST /api/auth/register` — register a new user
- `POST /api/auth/login` — login and get JWT token

#### Users
- `GET /api/users/me` — get current user information
- `PUT /api/users/me` — update user profile

#### Wallets
- `GET /api/wallets` — get all user wallets
- `POST /api/wallets` — create a new wallet
- `GET /api/wallets/{id}` — get wallet details
- `PUT /api/wallets/{id}` — update wallet
- `DELETE /api/wallets/{id}` — delete wallet

#### Transactions
- `GET /api/transactions` — get transaction history
- `POST /api/transactions` — create a new transaction
- `GET /api/transactions/{id}` — get transaction details
- `PUT /api/transactions/{id}` — update transaction
- `DELETE /api/transactions/{id}` — delete transaction

## 🧪 Testing

Run tests using Maven:

```bash
./mvnw test
```

Test coverage report:
```bash
./mvnw test jacoco:report
```

## 🔐 Security

### JWT Authentication
- All API endpoints are protected with JWT tokens (except `/api/auth/register` and `/api/auth/login`)
- Token expiration time: set by the `security.jwt.ttl-seconds` variable (in the application 360000000 seconds ≈ ~11.4 years)
- Secret key is stored in `application.properties`

### Data Validation
- Input data is validated at the controller level using Spring Validation
- Custom exceptions are handled by a global Exception Handler

## 📖 Database Management

### Flyway Migrations
Migrations are stored in `src/main/resources/db/migration/`:
- `V1_create_users_wallets_transactions.sql` — initial database schema

Migrations run automatically when the application starts.

### Database Initialization (for development)

If you use Docker Compose, the database initializes automatically:

```bash
docker-compose up -d
```

## 🔧 Configuration

### File `application.properties`

Key parameters:

| Parameter | Description | Default |
|-----------|-------------|---------|
| `spring.datasource.url` | Database connection URL | `jdbc:postgresql://localhost:5432/money_manager_db` |
| `spring.datasource.username` | Database user | `money_manager_user` |
| `spring.datasource.password` | Database password | `sav320801` |
| `security.jwt.secret` | JWT secret key | `13fe8c32a4c0ac72b3567a0ac3701f510ef78acac613225ea69a6d037ac0c65f` |
| `security.jwt.ttl-seconds` | JWT token lifetime | `360000000` |

## 📝 Usage Examples

### Registration
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "securePassword123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "securePassword123"
  }'
```

### Creating a Wallet
```bash
curl -X POST http://localhost:8080/api/wallets \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My Wallet",
    "currency": "USD",
    "balance": 1000.00
  }'
```

## 🐳 Docker

### Running with Docker Compose
```bash
docker-compose up -d
```

### Stopping
```bash
docker-compose down
```

## 📊 Database Structure

### Tables

- **users** — user information
- **wallets** — user wallets
- **transactions** — transaction history

Detailed schema can be found in `src/main/resources/db/migration/V1_create_users_wallets_transactions.sql`

## 🐛 Troubleshooting

### Database Connection Error
```
Make sure PostgreSQL is running and accessible at the address specified in application.properties
docker-compose ps  # To check Docker containers
```

### Build Errors
```bash
./mvnw clean install -X  # Run build in debug mode
```

### Clear Maven Cache
```bash
./mvnw clean
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

**Last Updated:** January 2026

**Version:** 0.0.1-SNAPSHOT

