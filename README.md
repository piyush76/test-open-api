# Spring Boot JOOQ IAM API

A Spring Boot REST API for Identity and Access Management (IAM) using JOOQ as the persistence layer.

## Features

- Spring Boot 3.2.0 with Java 17
- JOOQ for type-safe SQL queries
- H2 in-memory database for development
- Flyway for database migrations
- OpenAPI/Swagger documentation
- Comprehensive error handling
- RESTful API endpoints for user operations entities

## Architecture

The application follows a layered architecture:

- **Controller Layer**: REST endpoints with OpenAPI documentation
- **Service Layer**: Business logic with transaction management
- **Repository Layer**: Data access using JOOQ
- **Model Layer**: Entity classes with validation
- **Configuration**: JOOQ and database configuration
- **Exception Handling**: Global exception handling with custom error responses

## API Endpoints

### User Operations Entity Management

- `GET /api/v1/user-ops-entities` - Get all user operations entities
- `GET /api/v1/user-ops-entities/{personnelId}/{companyId}/{opsEntityId}` - Get specific entity
- `POST /api/v1/user-ops-entities` - Create new entity
- `PUT /api/v1/user-ops-entities/{personnelId}/{companyId}/{opsEntityId}` - Update entity
- `DELETE /api/v1/user-ops-entities/{personnelId}/{companyId}/{opsEntityId}` - Delete entity

## Building and Running

### Prerequisites

- Java 17 or higher
- Gradle 8.5 or higher

### Build

```bash
./gradlew clean build
```

### Run

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### API Documentation

Once running, access the Swagger UI at: `http://localhost:8080/swagger-ui.html`

## Database Schema

The application uses an H2 in-memory database with the following table:

### USER_OPS_ENTITY

- `PERSONNEL_ID` (BIGINT) - Personnel identifier
- `COMPANY_ID` (VARCHAR) - Company identifier  
- `OPS_ENTITY_ID` (VARCHAR) - Operations entity identifier
- `OPS_COMPANY_ID` (VARCHAR) - Operations company identifier
- `ADMIN_ROLE` (VARCHAR) - Administrative role (ADMIN, USER, MANAGER, SUPERVISOR)

Primary key: (PERSONNEL_ID, COMPANY_ID, OPS_ENTITY_ID)

## Technology Stack

- **Spring Boot 3.2.0** - Application framework
- **JOOQ 3.18.7** - Database access and type-safe SQL
- **H2 Database** - In-memory database for development
- **Flyway** - Database migration tool
- **SpringDoc OpenAPI** - API documentation
- **Lombok** - Boilerplate code reduction
- **Jakarta Validation** - Input validation
- **Gradle** - Build tool

## Project Structure

```
src/main/java/com/incora/api/iam/
├── IamApiApplication.java          # Main application class
├── config/
│   └── JooqConfig.java            # JOOQ configuration
├── controller/
│   └── UserOpsEntityController.java # REST controller
├── exception/
│   ├── ApiError.java              # Error response model
│   ├── ApiSubError.java           # Sub-error details
│   ├── EntityNotFoundException.java # Custom exception
│   └── RestExceptionHandler.java   # Global exception handler
├── model/
│   ├── AdminRole.java             # Admin role enum
│   └── UserOpsEntity.java         # Main entity model
├── repository/
│   ├── JooqUserOpsEntityDao.java  # JOOQ implementation
│   └── UserOpsEntityDao.java      # Repository interface
└── service/
    ├── UserOpsEntityService.java     # Service interface
    └── UserOpsEntityServiceImpl.java # Service implementation
```

## Development

This project was created based on OpenAPI specifications and follows Spring Boot best practices with comprehensive error handling, validation, and documentation.
