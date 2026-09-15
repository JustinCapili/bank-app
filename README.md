# Simple Bank App

Basic Spring Boot backend foundation.

## Requirements

- Java 17+
- Maven 3.9+

## Run

```bash
mvn spring-boot:run
```

The service starts on `http://localhost:8080`.

## Health endpoint

```bash
curl http://localhost:8080/api/v1/health
```

Expected response:

```json
{ "status": "UP", "service": "simple-bank-app" }
```

## Test

```bash
mvn test
```
