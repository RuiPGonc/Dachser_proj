# Dachser – Calculate Profit (Frontend)

Angular frontend for the **Calculate Profit** use case: a Finance Department user
picks a shipment, enters its income and costs, and the system calculates and
displays the profit or loss, together with the shipment's calculation history.

This implements Task 2 of the assessment (`Example_Tasks_Software_Engineer_v1.pptx`),
consuming the API exposed by the Task 1 Java backend.

## Tech stack

- Angular 17.3 (standalone components, no NgModules)
- Angular Material 17.3
- Bootstrap 5.3 (layout/grid utilities only)
- RxJS

## Project structure
src/app
├── app.component.ts/html/scss Root shell: header, sidebar (page name + Back button), <router-outlet>
├── app.routes.ts Route definitions (data.pageTitle / data.showBack per route)
├── app.config.ts Root providers (router, HttpClient, animations)
├── models/
│ ├── calculation.model.ts Costs, CalculationRequest, ProfitLoss
│ └── error.model.ts ErrorResponse (backend error body)
├── services/
│ └── service.ts ShipmentService – all HTTP calls to the backend
├── pages/
│ ├── start-page/ Shipment selection page (dropdown, no free text)
│ └── profit-loss-calculator-page/ Calculate Profit page (form + history)
└── components/
└── profit-loss-results/ Presentational, sortable results table


This maps onto the SOLID-oriented structure requested in the assessment:
**Model** → `models/`, **Service** → `services/service.ts`, **Components** →
`components/` (presentational) and `pages/` (containers), **Form** → the
reactive form in `profit-loss-calculator.component.ts`, **Routing** →
`app.routes.ts`.

## Features

- Shipment reference is chosen from a closed dropdown (`mat-select`) — no manual typing allowed.
- Income / Cost / Additional Cost inputs only accept numbers ≥ 0, with the native
  browser spinner arrows hidden.
- Validation errors and backend errors are surfaced through a snack bar popup
  (auto-dismissing), instead of silent red fields.
- The form resets to zero after every successful calculation.
- Results table only shows rows belonging to the currently open shipment, is
  sortable by every column (click the column header), defaults to most-recent-first
  on page load, and resets to insertion order right after a new calculation.
- App shell shows the current page name ("Shipment Select" / "Calculate Profit")
  and a "Back" button, only visible on the Calculate Profit page.

## Prerequisites

- Node 21
- The Task 1 backend running locally on `http://127.0.0.1:8080`

## Getting started

```bash
npm install
npm start
```

Navigate to `http://localhost:4200/`.

`npm start` runs `ng serve` with `proxy.conf.json`, which forwards every request
under `/api` to the backend (see `proxy.conf.json`). The `ShipmentService` calls:

| Frontend call                          | Proxied backend endpoint                          |
|-----------------------------------------|----------------------------------------------------|
| `GET /api/profitLoss/references`        | `GET http://127.0.0.1:8080/profitLoss/references`   |
| `GET /api/profitLoss?shipmentReference=`| `GET http://127.0.0.1:8080/profitLoss?shipmentReference=` |
| `POST /api/profitLoss/calculation`      | `POST http://127.0.0.1:8080/profitLoss/calculation` |

## Build

```bash
ng build
```

Artifacts are output to `dist/frontend`.

## Running unit tests

```bash
npm test
```

Runs the Jasmine specs via Karma (Chrome).

To generate a code coverage report:

```bash
ng test --code-coverage
```

Opens `coverage/frontend/index.html` for a full line-by-line coverage view.

## Generating code documentation

Component/service JSDoc comments can be turned into a browsable documentation
site with [Compodoc](https://compodoc.app/):

```bash
npm install --save-dev @compodoc/compodoc
npx compodoc -p tsconfig.json -s
```

Serves the generated docs, by default at `http://localhost:8080`.
