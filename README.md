# JustWords2 API - Asynchronous Ktor & MongoDB Backend

## Overview

JustWords2 API is a lightweight, non-blocking RESTful backend service built with **Kotlin** and **Ktor** (running on the Netty engine), designed to power the [JustWords2 Android Client](https://github.com/Wilie12/JustWords2).

The service provides stateless JWT authentication, vocabulary dataset distribution (Books, WordSets, and Words), user profile synchronization (daily goals and streaks), and offline-first learning history aggregation backed by **MongoDB** and **Kotlin Coroutines**.

## Architectural Highlights & Engineering Practices

* **Non-Blocking Reactive I/O:** Built from the ground up on Kotlin Coroutines. Both the HTTP transport layer (Ktor Netty) and the persistence layer (`kmongo-coroutine-core`) operate asynchronously using suspending functions, preventing thread pool starvation under concurrent load.
* **Dual-Token Stateless JWT Authentication:** Implements a separated token lifecycle using `Auth0 Java-JWT`. Short-lived `accessToken` (1-hour TTL) and long-lived `refreshToken` (365-day TTL) configurations are isolated via Koin named qualifiers and differentiated by custom `tokenType` claims. Refresh tokens are persisted in MongoDB (`MongoRefreshTokenRepository`) to validate session renewal requests.
* **Cryptographic Password Security:** User credentials are never stored in plain text. The `SHA256HashingService` generates a cryptographically strong 32-byte random salt via `SecureRandom` (`SHA1PRNG`) per user and hashes passwords using Apache Commons Codec (`SHA-256`).
* **Strict Persistence & Transport Separation:** Database entities (`@BsonId ObjectId`) are strictly isolated within `data/db/entity` packages and never leak directly into HTTP responses. Dedicated mapper extensions translate persistence models into `@Serializable` Data Transfer Objects (DTOs).
* **Type-Safe Functional Error Handling:** Business logic in controllers (`AuthController`, `UserController`, `UserHistoryController`, `WordsController`) avoids exception-driven control flow by returning a strongly-typed `Result<D, E: Error>` monad (`DataError.Auth`, `DataError.Insert`), which the routing layer maps deterministically to HTTP status codes.
* **Dependency Inversion & Modular Wiring:** All data sources (`UserDataSource`, `WordDataSource`, `UserHistoryDataSource`, `RefreshTokenRepository`) and security services are abstracted behind interfaces and wired via **Koin** Dependency Injection (`appModule`).

## Project Structure

```text
src/main/kotlin/example/com
├── Application.kt                  # Netty server entry point and plugin bootstrap
├── di/
│   └── appModule.kt                # Koin DI container configuration (DB, JWT configs, bindings)
├── plugins/
│   ├── DependencyInjection.kt      # Koin plugin installation and SLF4J logger setup
│   ├── Routing.kt                  # Global StatusPages exception handling and route registration
│   ├── Security.kt                 # JWT authentication realms (accessToken & refreshToken)
│   └── Serialization.kt            # Kotlinx JSON ContentNegotiation setup
├── auth/                           # Authentication routes, AuthController, and auth DTOs
├── user/
│   ├── data/
│   │   ├── db/                     # MongoDB data sources and BSON entities (User, UserInfo, UserWordHistory)
│   │   ├── hashing/                # Salted SHA-256 password hashing abstraction and implementation
│   │   └── token/                  # JWT generation/verification and MongoDB refresh token repository
│   ├── history/                    # Learning history synchronization routes, controller, and mappers
│   └── ...                         # User profile (streaks, daily goals) routes, controller, and DTOs
├── words/
│   ├── data/db/                    # MongoWordDataSource and BSON entities (Book, WordSet, Word)
│   ├── model/                      # Serializable response models
│   └── ...                         # Vocabulary browsing routes, WordsController, and mappers
└── util/                           # Functional Result<D, E> monad and DataError definitions
```

## Technology Stack

* **Language:** Kotlin 2.0.0
* **Framework:** Ktor Server 2.3.11 (Netty Engine)
* **Database:** MongoDB (via KMongo Coroutines 5.1.0)
* **Dependency Injection:** Koin 3.5.6 (`koin-ktor`, `koin-logger-slf4j`)
* **Security:** Ktor Auth JWT (`java-jwt`), Apache Commons Codec 1.17.0 (`SHA-256` + `SHA1PRNG`)
* **Serialization:** Kotlinx Serialization JSON
* **Logging:** Logback Classic 1.4.14
* **Build System:** Gradle Kotlin DSL

## Getting Started

### Prerequisites

* JDK 11 or higher
* Local **MongoDB** instance running on `mongodb://localhost:27017`

### 1. Environment Configuration

The server requires a `JWT_SECRET` environment variable to sign and verify HMAC256 JWT tokens. Export it in your terminal or configure it in your IDE run configuration before starting the application:

```bash
export JWT_SECRET="your-256-bit-super-secret-signing-key"
```

### 2. Database Preparation (Seeding Vocabulary Data)

The API is designed as a read-only provider for vocabulary catalogs (`Books`, `WordSets`, and `Words`). Before using the mobile client, seed your local `ktor-justwords2` MongoDB database with the following document structures:

#### 1. Insert a `Book` document (Collection: `book`)
```json
{
  "name": "Work & Career",
  "color": -14928072
}
```
*Note: MongoDB will automatically generate an `_id` (`ObjectId`). Copy its hex string for the next step.*

#### 2. Insert a `WordSet` document (Collection: `wordSet`)
```json
{
  "name": "Personality",
  "bookId": "6707f56059f9f0f47acb70e3",
  "numberOfGroups": 1
}
```
*Note: `bookId` must reference the hex string of the previously inserted `Book`.*

#### 3. Insert `Word` documents (Collection: `word`)
```json
{
  "sentence": "She is an ambitious career woman.",
  "wordPl": "ambitny",
  "wordEng": "ambitious",
  "setId": "6707f58059f9f0f47acb70e6",
  "groupNumber": 1
}
```
*Note: `setId` must reference the hex string of the `WordSet`. While fields are named `wordPl` (native language) and `wordEng` (target language with example `sentence`), they can store strings for any language pair.*

### 3. Running the Server

Use the repository-bound Gradle wrapper to launch the Netty server on port `8080`:

```bash
./gradlew run
```

---

## API Reference

### Authentication (Public Endpoints)

#### Register a New User
* **Endpoint:** `POST /register`
* **Responses:** `200 OK` (Registered), `400 Bad Request` (Invalid credentials), `409 Conflict` (Email already exists)
```json
{
  "username": "JohnDoe",
  "email": "user@email.com",
  "password": "Password123"
}
```

#### Authenticate (Login)
* **Endpoint:** `POST /login`
* **Responses:** `200 OK` (Returns `LoginResponse` with `accessToken`, `refreshToken`, and `userId`), `400 Bad Request` (User not found), `401 Unauthorized` (Invalid password)
```json
{
  "email": "user@email.com",
  "password": "Password123"
}
```

#### Refresh Access Token
* **Endpoint:** `POST /accessToken`
* **Responses:** `200 OK` (Returns `AccessTokenResponse` with new `accessToken`), `401 Unauthorized` (Invalid refresh token)
```json
{
  "refreshToken": "<refresh_token_from_login_response>",
  "userId": "6765ac88f033427a9ea31b1f"
}
```

---

### Vocabulary Catalog (Protected Endpoints)

All protected endpoints require the standard Bearer authentication header:
```http
Authorization: Bearer <accessToken>
```

| Method | Endpoint | Query Params | Response (`200 OK`) | Description |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/books` | None | `BooksResponse` (`books: List<BookSerializable>`) | Returns all available vocabulary books. |
| `GET` | `/sets` | None | `WordSetResponse` (`sets: List<WordSetSerializable>`) | Returns all word sets across all books. |
| `GET` | `/setsById` | `bookId` | `WordSetResponse` (`sets: List<WordSetSerializable>`) | Returns word sets belonging to a specific book. |
| `GET` | `/words` | `setId` | `WordsResponse` (`words: List<WordSerializable>`) | Returns words assigned to a specific word set. |

---

### User Profile & Streaks (Protected Endpoints)

#### Get User Profile Info
* **Endpoint:** `GET /getUserInfo?userId=6765ac88f033427a9ea31b1f`
* **Responses:** `200 OK` (Returns `UserInfoResponse`), `400 Bad Request` (User does not exist)

#### Update User Profile Info
* **Endpoint:** `POST /updateUserInfo`
* **Responses:** `200 OK` (Profile updated), `400 Bad Request` (Invalid payload), `401 Unauthorized` (User does not exist)
```json
{
  "userInfo": {
    "dailyStreak": 4,
    "bestStreak": 7,
    "currentGoal": 2,
    "dailyGoal": 5,
    "lastPlayedTimestamp": "2024-10-10T16:30:06.903Z",
    "lastEditedTimestamp": "2024-10-10T16:30:06.903Z",
    "username": "JohnDoe",
    "userId": "6765ac88f033427a9ea31b1f"
  }
}
```

---

### Learning History Synchronization (Protected Endpoints)

*Note: For history endpoints, the `userId` is extracted directly from the verified `JWTPrincipal` payload on the server side.*

#### Fetch User Learning History
* **Endpoint:** `GET /userHistory`
* **Responses:** `200 OK` (Returns `List<UserWordHistorySerializable>`), `400 Bad Request` (User does not exist)

#### Persist Completed Learning Session
* **Endpoint:** `POST /postUserHistory`
* **Responses:** `200 OK` (History saved), `400 Bad Request` (Invalid payload), `401 Unauthorized` (User does not exist), `409 Conflict` (Database insertion failure)
* *Note: Client-generated BSON `ObjectId` hex string is required in the `id` field to support offline-first idempotency.*
```json
{
  "bookName": "Work & Career",
  "bookColor": -14928072,
  "setName": "Personality",
  "groupNumber": 1,
  "dateTimeUtc": "2024-10-10T16:30:06.903Z",
  "perfectGuessed": 24,
  "wordListSize": 30,
  "id": "6708010e0930b24019febfbd"
}
```