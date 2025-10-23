# ingHub

A simple brokerage order service (Spring Boot + H2). Create BUY/SELL orders, list and cancel them, and view customer assets.

## Quick start (Windows / cmd.exe)

```cmd
mvnw.cmd clean test
mvnw.cmd spring-boot:run
```

- H2 Console: http://localhost:8080/h2-console
  - JDBC URL: jdbc:h2:mem:ingdb
  - User: sa, Password: (empty)

Note: H2 runs in-memory; data is reset when the app stops. To see data live, connect with the exact same JDBC URL above during the session.

## Authentication (JWT)

1) Login (get token):
- POST /api/auth/login
- Body:
```json
{"username":"admin","password":"password"}
```
- Response: { "token": "..." }

2) For all /api/** requests:
- Header: Authorization: Bearer <token>

Seed users (from data.sql): admin (id:1), alice (2), bob (3), cust1 (4)

## Core Endpoints

- Create order
  - POST /api/orders
  - Body example:
  ```json
  {"customerId":"4","assetName":"ABC","side":"BUY","size":10,"price":5}
  ```
  - createDate response format (UTC): "yyyy:MM:dd HH:mm:ss"

- List orders
  - GET /api/orders?customerId=4
  - Optional date range (LocalDate):
    - GET /api/orders?customerId=4&startDate=2025-10-01&endDate=2025-10-15

- Cancel order
  - DELETE /api/orders/{id}
  - Only PENDING orders can be canceled.

- Customer assets
  - GET /api/assets?customerId=4

## Business rules (brief)
- BUY/SELL orders are created as PENDING.
- All cash operations use the TRY asset.
- BUY: checks usable TRY >= price*size and deducts it.
- SELL: adds price*size to usable TRY (example model).
- CANCEL: reverts the corresponding TRY change and sets status to CANCELED.

## Tests
```cmd
mvnw.cmd clean test
```

## Postman Collection
- File: `postman/ingHub.postman_collection.json`
- Import in Postman: Import -> File -> select the JSON file above.
- Collection variables:
  - `baseUrl` (default: http://localhost:8080)
  - `token` (auto-filled after running Auth -> Login)
  - `customerId` (default: 4)
  - `assetName` (default: TRY)
  - `orderId` (auto-filled after running Orders -> Create Order)
- Usage flow:
  1) Run Auth -> Login to populate `{{token}}`.
  2) Run Orders -> Create Order (BUY) to create an order and set `{{orderId}}`.
  3) Run Orders -> List Orders (by customer) or (date range) as needed.
  4) Run Orders -> Cancel Order (last created) which uses `{{orderId}}`.
  5) Run Assets -> Get Assets by Customer.

## Persistence note
- Default: H2 in-memory (ephemeral). To persist across restarts, you can switch to file mode, e.g. `jdbc:h2:file:./ingdb;AUTO_SERVER=TRUE`, and prefer `spring.jpa.hibernate.ddl-auto=update`.
