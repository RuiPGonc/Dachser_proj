# Dachser Shipment – Calculate Profit

Full-stack implementation of the **Calculate Profit** use case for Dachser's
logistics income/cost evaluation system: given the income and costs recorded
for a shipment, the system calculates and stores its profit or loss.

This repository contains the two tasks of the assessment
(`Example_Tasks_Software_Engineer_v1.pptx`):

```
Shipment/
├── backend/    Task 1 — Java 17 / Spring Boot 3.1.5 REST API
└── frontend/   Task 2 — Angular 17 / Angular Material UI
```

Each project has its own detailed README:

- [`backend/README.md`](backend/README.md) — architecture, database schema,
  API reference, running the app, tests, coverage, Javadoc.
- [`frontend/README.md`](frontend/README.md) — architecture, features,
  running the app, tests, coverage, JSDoc (Compodoc).

## Use case

> Finance Department requests a profit or loss calculation for a shipment →
> system records the income and cost data submitted → system calculates
> `profit = income - cost` → result is stored and displayed in the UI.

Full requirements and the original UI prototype are in
`Use_Case_Calculate_Profit_Task_Assessment_SE.pdf`.

## Running the whole stack locally

Start the backend first (it listens on port `8080`), then the frontend
(which proxies `/api` to it):

```bash
# Terminal 1 — backend
cd backend
./mvnw spring-boot:run

# Terminal 2 — frontend
cd frontend
npm install
npm start
```

Open `http://localhost:4200`.

## Deliverables checklist (per assessment slides)

| Deliverable                                    | Where                                                            |
|-------------------------------------------------|-------------------------------------------------------------------|
| Backend source (build & run)                     | `backend/` (Maven wrapper included)                               |
| SQL schema, relations, indexes                   | `backend/src/main/resources/schema.sql`                            |
| Insertion scripts                                 | `backend/src/main/resources/data.sql`                              |
| In-memory database                                | H2 (`backend/src/main/resources/application.properties`)           |
| Postman collection                                | `backend/Shipment-ProfitLoss.postman_collection.json`              |
| Backend unit tests                                | `backend/src/test/java/...` (`./mvnw test`)                        |
| Backend unit test coverage                        | `./mvnw test` → `backend/target/site/jacoco/index.html`             |
| Frontend source (build & serve)                   | `frontend/` (Angular CLI)                                          |
| Jest or Jasmine+Karma unit tests                  | `frontend/src/app/**/*.spec.ts` (`npm test`)                       |
| Unit test coverage report                         | `npm test -- --code-coverage` → `frontend/coverage/frontend/index.html` |
| Documentation & comments                          | JSDoc in frontend `.ts` files, Javadoc in backend `.java` files    |