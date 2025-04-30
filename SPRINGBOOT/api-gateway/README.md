# API Gateway - SOLID Principles Implementation

This project demonstrates the application of the SOLID principles in designing a clean, maintainable, and extensible API Gateway architecture.

---

## ✅ Applying SOLID Principles

### 1. Single Responsibility Principle (SRP)

Each class has a single, clearly defined responsibility:

- **Controllers**: Handle HTTP requests and responses (e.g., `FallbackController`).
- **Filters**: Manage authentication and request filtering (e.g., `AuthenticationFilter`).
- **Services**: Contain business logic (e.g., `IdentityService`).
- **Clients**: Handle communication with external services (e.g., `IdentityClient`).

> Example:  
> `AuthenticationFilter` is dedicated to filtering and validating requests, while `IdentityService` is responsible for communication with the identity service.

---

### 2. Open/Closed Principle (OCP)

The system is designed to be open for extension but closed for modification:

- New fallback mechanisms can be added by creating endpoints in `FallbackController` without modifying existing logic.
- Additional authentication methods can be introduced by extending `AuthenticationFilter` or adding new filters.

---

### 3. Liskov Substitution Principle (LSP)

Subtypes can replace their base types without affecting program correctness:

- All implementations of the `IdentityClient` interface can be used interchangeably, ensuring consistent behavior when accessing identity-related functionality.

---

### 4. Interface Segregation Principle (ISP)

Interfaces are kept focused and minimal:

- `IdentityClient` only includes methods relevant to identity operations such as token introspection and user info retrieval.
- This ensures clients depend only on what they actually use.

---

### 5. Dependency Inversion Principle (DIP)

High-level modules do not depend on low-level modules; both depend on abstractions:

- `AuthenticationFilter` relies on the `IdentityService` interface, not on concrete implementations.
- This promotes flexibility, testability, and the ability to easily swap implementations.

---

## 🔁 Example Request Flow

1. A client sends a request to the API Gateway.
2. `AuthenticationFilter` validates the request and token via the `identity-service`.
3. If valid, the request is routed to the appropriate microservice (e.g., `patient-service`, `billing-service`).
4. If the target service is unavailable, `FallbackController` handles the fallback response.

---

By following the SOLID principles, this API Gateway is modular, maintainable, and easy to extend for future needs.
