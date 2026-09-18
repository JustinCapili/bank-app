# JWT Authentication Implementation Plan

This outlines how to add JWT-based authentication to the simple-bank-app repo. No code has been changed — this is a plan only.

## Current state (why this is needed)

- No `spring-boot-starter-security` dependency in [pom.xml](../pom.xml).
- `User` has no password field; "login" (`GET /api/users/{id}`) just looks up a user by ID with no credential check.
- All `/api/**` endpoints in `UserController` and `AccountController` are unauthenticated and unauthorized — any caller can read/deposit/withdraw on any account by guessing an ID.
- Frontend (`Login.js`, `CreateAccount.js`, `api.js`, `App.js`) has no concept of a token or auth header.

## Decisions to lock in before implementing

1. **Auth model**: add a real `password` field to `User` (hashed with BCrypt), require it on signup and login, then issue a JWT — as opposed to just token-izing the existing no-password lookup.
2. **Protected scope**: everything under `/api/**` requires a valid JWT except `POST /api/users` (signup) and `POST /api/auth/login`.
3. **Ownership enforcement**: a valid token alone isn't enough — endpoints must also confirm the authenticated user owns the resource (their own user record / their own accounts), not just any logged-in user.

## Backend changes

### 1. Dependencies (`pom.xml`)

- `spring-boot-starter-security`
- `io.jsonwebtoken:jjwt-api`, `jjwt-impl` (runtime), `jjwt-jackson` (runtime) — version 0.12.x
- `spring-security-test` (test scope) for MockMvc auth support

### 2. Config (`application.properties`)

- `jwt.secret=${JWT_SECRET:<dev-only-default>}` — must be overridden via env var in real deployments (32+ bytes for HMAC-SHA256).
- `jwt.expiration-ms=${JWT_EXPIRATION_MS:3600000}`

### 3. New `security` package

- **`JwtService`**: wraps `io.jsonwebtoken.Jwts`. `generateToken(int userId)` (subject = userId), `extractUserId(String token)`, `isValid(String token)`.
- **`JwtAuthenticationFilter`** (`OncePerRequestFilter`): reads `Authorization: Bearer <token>`, validates via `JwtService`, and if valid sets a `UsernamePasswordAuthenticationToken(userId, null, [])` on the `SecurityContext` (principal = `Integer` userId).
- **`SecurityConfig`**: `SecurityFilterChain` bean — CSRF disabled (stateless API), `SessionCreationPolicy.STATELESS`, permit `POST /api/users` and `POST /api/auth/login`, require auth on everything else, register `JwtAuthenticationFilter` before `UsernamePasswordAuthenticationFilter`. Also defines the `PasswordEncoder` bean (`BCryptPasswordEncoder`).

### 4. `User` model

- Add `private String password;` + constructor overload + `getPassword()` annotated `@JsonIgnore` (never serialize the hash to clients).

### 5. `UserRepository`

- Inject `PasswordEncoder`.
- `createUser(name, email, password)`: reject blank password, hash with the encoder, store `password` field in the Mongo document.
- `mapUser(document)`: include the stored password hash when reconstructing `User` (needed for verification, never sent back over JSON due to `@JsonIgnore`).
- New `verifyPassword(int userId, String rawPassword)`: fetch user, `passwordEncoder.matches(...)`.

### 6. `UserService`

- `createUser(name, email, password)` → delegate to repo.
- `authenticate(int userId, String password)` → verify via repo, return `User` or `null`.

### 7. `UserController`

- `UserCreationRequest` record gains a `password` field.
- `POST /api/users`: create user, then immediately issue a JWT via `JwtService`, return `{ user, token }` (auto-login on signup).
- `GET /api/users/{id}`: require the authenticated principal's userId to equal `{id}` (403 otherwise) — inject `Authentication` param.

### 8. New `AuthController`

- `POST /api/auth/login` with `{ userId, password }` → `UserService.authenticate` → 401 if null, otherwise issue JWT and return `{ user, token }`.

### 9. `AccountController` / `AccountService` / `AccountRepository` — ownership checks

- `AccountRepository`: add `getOwnerUserId(int accountId)` (reads the `userId` field already stored on the account document).
- `AccountService`: expose `getOwnerUserId`.
- `AccountController`: inject `Authentication` into every endpoint and enforce:
  - `GET /accounts/{id}`, `PUT /accounts/{id}/deposit`, `PUT /accounts/{id}/withdraw`, `GET /accounts/{id}/transactions` → caller must own that account (403 otherwise).
  - `GET /users/{userId}/accounts` and `POST /accounts` (via `request.userId()`) → caller must match `{userId}`.

## Frontend changes

### `api.js`

- Add `login(userId, password)` → `POST /api/auth/login`.
- Update `createUser(name, email, password)`.
- Add token storage helpers (`getToken`/`setToken`/`clearToken`, backed by `localStorage`).
- Attach `Authorization: Bearer <token>` header to every authenticated request (accounts, deposit/withdraw, transactions, `GET /users/{id}`).
- On `401`/`403`, clear the stored token so the app can redirect to login.

### `Login.js`

- Add a password field; call `login(userId, password)` instead of `getUser(userId)`; store the returned token; then call `onLoginSuccess(user)`.

### `CreateAccount.js`

- Add a password field; call the updated `createUser(name, email, password)`; store the returned token; call `onUserCreated(user)`.

### `App.js`

- Clear the stored token on logout.
- Known gap to flag to the user: since `user` state lives only in memory, a full page refresh still loses the session even though the token persists in `localStorage`, unless a `/api/auth/me`-style endpoint (or JWT decode) is added later to rehydrate `user` from the stored token on load.

## Test impact (must be addressed alongside the above)

- `AccountControllerTest` (`@WebMvcTest`) currently sends unauthenticated requests and expects 200s. Once `SecurityConfig` is picked up by the web-layer test slice, these will start returning 401/403. Either:
  - Add `@AutoConfigureMockMvc(addFilters = false)` to keep it a pure controller-logic test, **or**
  - Use `spring-security-test`'s `SecurityMockMvcRequestPostProcessors.authentication(...)` per request and stub `AccountService.getOwnerUserId(...)` to match.
- `AccountRepositoryCreationTest` uses the no-arg `AccountRepository()` constructor — unaffected by security, but if `UserRepository`'s no-arg constructor changes signature (e.g., to accept a `PasswordEncoder`), double check nothing else instantiates it directly.
- `SimpleBankApplicationTests` (`@SpringBootTest`) will pick up the new `jwt.secret`/`jwt.expiration-ms` properties — safe as long as the defaults in `application.properties` don't require an env var to be set for context load.

## Suggested implementation order

1. Backend: dependencies + config properties.
2. `security` package (`JwtService`, filter, `SecurityConfig`).
3. `User` model password field.
4. `UserRepository` / `UserService` password + auth methods.
5. `UserController` + new `AuthController`.
6. `AccountController` ownership checks (+ repo/service support).
7. Fix/update existing tests.
8. Frontend: `api.js`, `Login.js`, `CreateAccount.js`, `App.js`.
9. Manual end-to-end verification: signup → token issued → login → protected calls succeed with token, fail (401/403) without it or with someone else's account ID.
