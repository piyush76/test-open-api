# User Operations Entity API

A Spring Boot REST API for managing user operations entity assignments with OpenAPI documentation.

## Features

- RESTful API for UserOpsEntity management
- OpenAPI/Swagger documentation
- JOOQ for type-safe database queries
- H2 in-memory database for development
- Spring Security with basic authentication
- Comprehensive error handling
- Unit tests

## Technology Stack

- **Spring Boot 3.2.0** with Java 17
- **Gradle** build system with JOOQ plugin
- **H2 Database** for development and testing
- **JOOQ** for database access
- **SpringDoc OpenAPI** for API documentation
- **Lombok** for reducing boilerplate code
- **Spring Security** for authentication

## API Endpoints

The API exposes the following endpoints under `/iam`:

- `GET /iam/users/{userId}/companies/{companyId}/ops-entity` - Get user operations entity
- `PUT /iam/users/{userId}/companies/{companyId}/ops-entity` - Update admin role

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 8.5 or higher (or use the included wrapper)

### Building the Project

```bash
./gradlew build
```

### Running the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`.

### Default Authentication

- Username: `admin`
- Password: `admin`

## API Documentation

Once the application is running, you can access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **H2 Console**: http://localhost:8080/h2-console

## Database

The application uses an H2 in-memory database with sample data:

- Personnel ID 1, Company COMP1 → Admin role
- Personnel ID 2, Company COMP1 → Grant Admin role  
- Personnel ID 1, Company COMP2 → Admin role

## Testing

Run the tests with:

```bash
./gradlew test
```

## Example API Calls

### Get User Operations Entity

```bash
curl -u admin:admin http://localhost:8080/iam/users/1/companies/COMP1/ops-entity
```

### Update Admin Role

```bash
curl -u admin:admin -X PUT \
  -H "Content-Type: application/json" \
  -d '"Grant Admin"' \
  http://localhost:8080/iam/users/1/companies/COMP1/ops-entity
```

## Project Structure

```
src/main/java/com/userops/api/
├── UserOpsApiApplication.java          # Main application class
├── config/                             # Configuration classes
├── controller/                         # REST controllers
├── service/                           # Business logic
├── repository/                        # Data access layer
├── model/                            # Domain models
└── exception/                        # Error handling
```

## Configuration

Key configuration properties in `application.properties`:

- Server port: `8080`
- Database: H2 in-memory
- Security: Basic authentication enabled
- OpenAPI: Swagger UI enabled
- Logging: Debug level for JOOQ and application packages
