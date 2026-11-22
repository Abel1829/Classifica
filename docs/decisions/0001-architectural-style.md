# ADR 0001: Modular monolith with a React single-page app

- **Status:** Accepted
- **Related:** `docs/architecture/architecture-characteristics.md`

## Context

Classifica needs a public website, an admin area for organizers, referees, and team managers, and a REST API with a relational database. The top architecture characteristics are data consistency, security, and modularity. The constraints are one developer, about one month, and about 2 hours of work per day. The whole system must start with `docker compose up`.

## Decision

The system is built as:

1. **One Spring Boot application (a modular monolith)** that exposes a REST API. It is split into modules (tournaments, teams, matches, standings, users, news), each in its own package with a small public interface.
2. **One PostgreSQL database** shared by all modules, accessed through each module's own repositories.
3. **A separate React single-page application** for both the public site and the admin area. It communicates with the backend only through the REST API.
4. **Apache Camel routes inside the backend** for integration tasks (for example importing data or sending notifications).
5. **Docker Compose** to run the database, the API, and the frontend together.

Module boundaries are protected by an automated architecture test, so the monolith stays modular.

## Alternatives considered

| Option | Advantages | Disadvantages | Verdict |
|---|---|---|---|
| **Modular monolith (chosen)** | One deployable unit, simple transactions, easy to test and run, fits one developer | Modules cannot scale independently | Chosen |
| **Microservices** | Independent scaling and deployment | Distributed transactions make conflict checks and standings hard; much more infrastructure; far too heavy for one developer in a month | Rejected |
| **Plain layered monolith without modules** | Simplest to start | Features become entangled; harder to add optional extras later; weaker on modularity | Rejected |
| **Server-rendered pages instead of React** | Less frontend code | Weaker fit for the live-scores interface (theme, favourites, search, polling); less separation between API and UI | Rejected |

## Consequences

**Positive**
- Scheduling and result rules run in a single database transaction, which supports the consistency characteristic.
- One codebase and one deployment keep the work achievable in the available time.
- The REST API is a clear contract between frontend and backend, and it matches the HTTP topic of the course.
- Modules can be built and tested one at a time.

**Negative**
- The whole application scales as one unit. This is acceptable because horizontal scalability is an explicit non-goal.
- Module boundaries depend on discipline, so they are enforced with an architecture test.
- Two codebases (backend and frontend) must be kept in step; the REST API is documented in `docs/api.md`.

## Revisit when

- Load requires scaling one module independently, or a second team works on the system. A module could then be extracted into its own service, because its boundary is already defined.
