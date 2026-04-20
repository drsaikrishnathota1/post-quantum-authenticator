# post-quantum-authenticator

Java microservice (Spring Boot) with **one endpoint**:

- `POST /verify` with `username` + `password`
- Password verification uses **Argon2id**
- A **PQC (Dilithium) sign+verify step** is performed inside the same request as an extra “post-quantum” check (no extra endpoints)

## Prereqs

- Java 21+
- Maven 3.9+

## Run

```bash
cd /Users/sai/Documents/post-quantum-authenticator
mvn spring-boot:run
```

Service runs on `http://localhost:8080`.

## Verify (Postman/curl)

```bash
curl -i -X POST http://localhost:8080/verify \
  -H 'Content-Type: application/json' \
  -d '{"username":"rocket","password":"89p13"}'
```

## Config

Demo credentials are configured in `src/main/resources/application.yml`.

