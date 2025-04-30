# Identity service

This microservice is responsible for:

- Onboarding users
- Roles and permissions
- Authentication

## Tech stack

- Build tool: maven >= 3.9.5
- Java: 21
- Framework: Spring boot 3.2.x
- DBMS: MySQL

## Prerequisites

- Java SDK 21
- A MySQL server

## Start application

`mvn spring-boot:run`

## Build application

`mvn clean package`

## APPLY SOLID PRINCIPLES

### 1. **Single Responsibility Principle (SRP)**

Each class in the service has a single responsibility:

- **Controllers** handle HTTP requests and responses.
- **Services** encapsulate business logic.
- **Repositories** interact with the database.
- **dto**

For example, the `UserService` class focuses solely on user-related business logic, while the `UserRepository` handles database operations.

### 2. **Open/Closed Principle (OCP)**

The service is designed to be open for extension but closed for modification:

- New features, such as additional authentication mechanisms, can be added by implementing new interfaces or extending existing classes without modifying existing code.
- For example, adding a new role type can be achieved by extending the `Role` class or adding new logic in a separate service.

### 3. **Interface Segregation Principle (ISP)**

Interfaces are designed to be specific to the needs of the client:

- Instead of having a single large interface, smaller, more focused interfaces are used. For example, `UserRepository` and `RoleRepository` are separate, ensuring that each repository only handles its specific entity.

### 4. **Dependency Inversion Principle (DIP)**

High-level modules do not depend on low-level modules; both depend on abstractions:

- Services depend on interfaces rather than concrete implementations. For example, the `UserService` depends on the `UserRepository` interface, making it easy to swap out implementations (e.g., for testing).

By adhering to these principles, the `identity-service` is modular, maintainable, and extensible.
