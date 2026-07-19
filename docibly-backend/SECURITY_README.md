# Security Integration Guide

## Overview

This project includes a comprehensive security implementation with JWT-based authentication using RSA asymmetric encryption. The security layer provides:

- User authentication and registration
- JWT token-based authorization (Access & Refresh tokens)
- Role-based access control (RBAC)
- Secure password encryption with BCrypt
- Full audit trail (created by, last modified by, timestamps)
- UUID-based entity IDs
- Email validation with disposable email blocking
- Spring Security integration

## Key Components

### 1. Authentication Flow

#### Registration
- **Endpoint**: `POST /api/v1/auth/register`
- **Body**: `RegistrationRequest` (firstName, lastName, email, phoneNumber, password, confirmPassword)
- **Validations**:
  - Strong password requirements (min 8 chars, uppercase, lowercase, digit, special char)
  - Non-disposable email addresses
  - Valid phone number format
  - Unique email and phone number

#### Login
- **Endpoint**: `POST /api/v1/auth/login`
- **Body**: `AuthenticationRequest` (email, password)
- **Response**: `AuthenticationResponse` (accessToken, refreshToken, tokenType)

#### Token Refresh
- **Endpoint**: `POST /api/v1/auth/refresh`
- **Body**: `RefreshRequest` (refreshToken)
- **Response**: New `AuthenticationResponse` with fresh access token

### 2. User Management

Protected endpoints (require Bearer token):

- **Update Profile**: `PATCH /api/v1/users/profile`
- **Change Password**: `PATCH /api/v1/users/password`
- **Deactivate Account**: `POST /api/v1/users/deactivate`
- **Reactivate Account**: `POST /api/v1/users/reactivate`
- **Delete Account**: `DELETE /api/v1/users`

### 3. Security Configuration

#### JWT Tokens
- **Access Token**: Valid for 24 hours (configurable via `app.security.jwt.access-token-expiration`)
- **Refresh Token**: Valid for 7 days (configurable via `app.security.jwt.refresh-token-expiration`)
- **Algorithm**: RSA-2048 asymmetric encryption
- **Keys**: Auto-generated on first run in `src/main/resources/keys/`

#### Public Endpoints
The following endpoints are accessible without authentication:
- `/api/v1/auth/**` - Authentication endpoints
- `/swagger-ui/**` - API documentation
- `/v3/api-docs/**` - OpenAPI specification
- `/actuator/**` - Health checks and metrics

### 4. Database Schema

#### Users Table
- `id` (VARCHAR UUID) - Primary key
- `ref` (VARCHAR) - Optional reference
- `first_name`, `last_name`, `email`, `phone_number`, `password`
- `date_of_birth`, `profile_picture_url`
- Boolean flags: `is_enabled`, `is_account_locked`, `is_credential_expired`, `is_email_verified`, `is_phone_verified`
- Audit fields: `created_date`, `last_modified_date`, `created_by`, `last_modified_by`

> **Default Role Assignment**: When a new user registers, they are automatically assigned the `ROLE_USER` role. If this role doesn't exist in your configuration, the system assigns the first available role from your role list.

#### Roles Table
- `id` (VARCHAR UUID) - Primary key
- `name` (VARCHAR) - Role name (e.g., ROLE_USER, ROLE_ADMIN, ROLE_MODERATOR)
- Audit trail fields

**Configured Roles:**
- `ROLE_ADMIN`
- `ROLE_USER`

> **Note**: Roles are defined in the project configuration YAML and automatically created during database initialization. New users are assigned the `ROLE_USER` role by default, or the first available role if `ROLE_USER` doesn't exist.

#### Users_Roles (Join Table)
- Many-to-many relationship between users and roles

### 5. RSA Key Generation

Keys are automatically generated on application startup if they don't exist:
- **Location**: `src/main/resources/keys/`
- **Files**: `private_key.pem`, `public_key.pem`
- **Algorithm**: RSA-2048

> **Note**: For production, consider using a secure key management service or vault.

### 6. Configuration Properties

Add to your `application.yml`:

```yaml
app:
  security:
    jwt:
      access-token-expiration: 86400000  # 24 hours
      refresh-token-expiration: 604800000  # 7 days
    disposable-email: 10minutemail,20minutemail,33mail,guerrillamail,mailinator
```

## Usage Examples

### 1. Register a New User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \\
  -H "Content-Type: application/json" \\
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+1234567890",
    "password": "SecurePass123!",
    "confirmPassword": "SecurePass123!"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \\
  -H "Content-Type: application/json" \\
  -d '{
    "email": "john.doe@example.com",
    "password": "SecurePass123!"
  }'
```

Response:
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiJ9...",
  "refresh_token": "eyJhbGciOiJSUzI1NiJ9...",
  "token_type": "Bearer"
}
```

### 3. Access Protected Endpoint

```bash
curl -X PATCH http://localhost:8080/api/v1/users/profile \\
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \\
  -H "Content-Type: application/json" \\
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "dateOfBirth": "1990-01-01"
  }'
```

## Security Best Practices

1. **Never commit RSA keys** - Add `src/main/resources/keys/` to `.gitignore`
2. **Use HTTPS in production** - All JWT tokens should be transmitted over HTTPS
3. **Rotate keys regularly** - Implement key rotation strategy for production
4. **Monitor failed login attempts** - Implement rate limiting and account lockout
5. **Use strong password policies** - Already enforced by validation
6. **Keep tokens short-lived** - Configure appropriate expiration times
7. **Secure refresh token storage** - Store refresh tokens securely on client side

## Role-Based Access Control (RBAC)

This project uses a fine-grained permission model with three scopes per role per entity:

### Permission Scopes

| Scope | Behavior | Use Case |
|---|---|---|
| `all` | Operation allowed on ANY resource, no ownership check | Admin full CRUD |
| `own` | Operation allowed only on resources where `createdBy == currentUser` | User editing own records |
| `scoped` | Operation filtered by a configurable `scopeField` (e.g. `departmentId`) | Department/tenant-level access |

### How It Works

**Single-resource endpoints** (`findById`, `update`, `deleteById`):  
The `CustomPermissionEvaluator` runs a JPQL ownership query at the Spring Security level:

```
SELECT COUNT(e) > 0 FROM Entity e WHERE e.id = :id AND e.{scopeField} = :userId
```

**List endpoints** (`findAll`, `findPaginatedByCriteria`):  
The service layer injects a JPA `Specification` predicate that filters results by the appropriate scope field. Users who have `all: [READ]` see all rows; users with only `own`/`scoped` READ see only their scoped rows.

### YAML Configuration

```yaml
permissions:
  Document:
    ADMIN:
      all: ["CREATE", "READ", "UPDATE", "DELETE"]
    MEMBER:
      all: ["CREATE"]
      own: ["UPDATE", "DELETE"]
      scoped: ["READ"]
      scopeField: "departmentId"
```

**Rules:**
- `CREATE` can only be in the `all` scope (resource doesn't exist yet)
- An operation can only appear in one scope per role (no duplicates across `all`/`own`/`scoped`)
- If `scoped` is used without `scopeField`, it defaults to `createdBy`
- `own`-scoped READ on list endpoints is auto-filtered by `createdBy` for users who lack `all: [READ]`

**Role evaluation is most-permissive-wins**: if a user has multiple roles and any role grants `all` access, evaluation stops immediately and grants full access.

### Generated Permission Files

| File | Purpose |
|---|---|
| `PermissionConfig.java` | Bakes YAML permissions into `Map<String, EntityPermissions>` at startup |
| `CustomPermissionEvaluator.java` | Spring Security `PermissionEvaluator` — enforces per-resource access |
| `RolePermissions.java` | POJO: `allOperations`, `ownOperations`, `scopedOperations`, `scopeField` |
| `EntityPermissions.java` | POJO: maps role name → `RolePermissions` |

> **Warning**: `all: [READ]` returns ALL rows in the table. Use `scoped` with `scopeField` for tenant/department-level data isolation, or implement custom Specifications for complex filtering needs.

### Role Combination Warning

ARCHEON uses a **most-permissive-wins** role evaluation model. If a user holds multiple roles and any role grants `all` scope on an entity, the user has full table access to that entity regardless of what other roles restrict.

Assigning cumulative roles (e.g. MEMBER + AUDITOR) may grant broader access than intended. Audit multi-role user assignments in production.

For `scoped` and `own` scopes, row-level filters are combined with OR logic: a user with both `scoped: [READ]` (department scope) and `own: [READ]` will see both their own records and their department's records. This is additive — more roles = more data visible, never less.

## API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

All endpoints are documented with:
- Request/Response schemas
- Validation rules
- Security requirements
- Example payloads

## Troubleshooting

### JWT Token Issues
- Ensure keys are properly generated in `src/main/resources/keys/`
- Check token expiration times in configuration
- Verify Bearer token format: `Authorization: Bearer <token>`

### Database Issues
- Run Flyway migrations: Automatically applied on startup
- Check database connection in application-{profile}.yml
- Ensure PostgreSQL is running for prod/test profiles

### Email Validation Failing
- Update disposable email list in configuration
- Check email format matches standard pattern
