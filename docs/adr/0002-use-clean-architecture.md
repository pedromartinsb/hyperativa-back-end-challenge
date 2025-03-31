# ADR 0002: Use Clean Architecture

## Status

Accepted

## Context

We need to choose an architectural pattern for the Visa Credit Card API that will:

- Separate concerns effectively
- Make the codebase maintainable and testable
- Allow for easy changes in external dependencies
- Keep business rules independent of frameworks

## Decision

We will use Clean Architecture as our primary architectural pattern, with the following layers:

- Domain: Core business entities and rules
- Application: Use cases and application services
- Adapter: External system adapters
- Infrastructure: Technical implementations

## Consequences

### Positive

- Clear separation of concerns
- Business rules are independent of external frameworks
- Easy to test business logic
- Flexible to change external dependencies
- Clear boundaries between layers

### Negative

- More initial setup required
- Need to maintain clear boundaries between layers
- May require more boilerplate code
- Team needs to understand and follow the pattern
- May be overkill for very simple features
