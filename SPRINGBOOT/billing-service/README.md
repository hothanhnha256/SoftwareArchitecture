# Billing Service - SOLID Principles Implementation

This project showcases how the SOLID principles are applied to build a modular and maintainable billing service system.

---

## ✅ Applying SOLID Principles

### 1. Single Responsibility Principle (SRP)

Each class has one clearly defined responsibility:

- **Controllers**: Handle HTTP requests and responses (e.g., `PaymentController`).
- **Services**: Encapsulate business logic (e.g., `HospitalService`).
- **Factories**: Manage creation of billing strategies (e.g., `BillingServiceFactory`).
- **Billing Implementations**: Process specific payment methods (e.g., `CashBillingService`, `CreditCardBillingService`, `MasterCardBillingService`).

> Example:  
> `PaymentController` handles request routing, while each billing service implementation processes its own payment logic independently.

---

### 2. Open/Closed Principle (OCP)

The system is designed to be open for extension, but closed for modification:

- Adding a new payment method (e.g., `PayPal`) only requires implementing a new class that adheres to the `BillingService` interface.
- No need to alter existing code when adding new functionality.

---

### 3. Liskov Substitution Principle (LSP)

Subtypes can replace base types without affecting system behavior:

- All implementations of the `BillingService` interface (e.g., `CashBillingService`, `CreditCardBillingService`) can be used interchangeably.
- The rest of the system remains unaware of the specific implementation details.

---

### 4. Interface Segregation Principle (ISP)

Interfaces are minimal and specific to client needs:

- `BillingService` defines only the methods necessary for processing payments.
- This ensures that each billing implementation adheres strictly to what is required.

---

### 5. Dependency Inversion Principle (DIP)

High-level modules depend on abstractions, not concrete implementations:

- `HospitalService` depends on the `BillingService` interface.
- `BillingServiceFactory` is responsible for providing the correct billing implementation, enabling flexibility and ease of testing.

---

## 🔁 Example Workflow

1. A client sends a payment request to the `/payment/{option}` endpoint.
2. `PaymentController` receives and forwards the request to `HospitalService`.
3. `HospitalService` queries `BillingServiceFactory` for the correct billing strategy based on the `{option}` parameter.
4. The selected `BillingService` implementation processes the payment and returns the result.

---

By following the SOLID principles, this billing service is:

- ✅ Easy to extend with new payment methods
- 🛠️ Maintainable and testable
- 🔄 Adaptable to changing business requirements
