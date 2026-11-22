# Architecture Characteristics

**Project:** Classifica, a tournament management system
**Status:** Draft v1

## 1. Purpose

This document lists the architecture characteristics (the "-ilities") that Classifica must support, ranks them, and explains how each one is achieved and checked. These choices drive the architectural style (see the decision records in `docs/decisions/`).

## 2. System summary

Classifica lets **organizers** create tournaments and venues, add referees and team managers, publish news, and draw matches (date, time, venue, referee). **Referees** record match events and results. **Team managers** manage their team, players, and line-ups. The **public** uses a live-scores style website without logging in (scores, standings or brackets, teams, players, news, search, favourites, language, and theme).

Constraints: one developer, about one month, about 2 hours per day. Delivery on a single machine with `docker compose up`.

## 3. How the characteristics were chosen

Each candidate characteristic was taken from a requirement, not from a generic list. A characteristic is **driving** if the system fails its purpose without it, and **supporting** if it matters but does not shape the structure.

## 4. Driving characteristics (top 3)

### 4.1 Data consistency

- **Why it matters:** The core promise of the system is that a schedule is valid and the standings are correct. A venue or referee must never be double-booked, a team must never play two matches at once, and standings must always match the recorded results.
- **Linked requirements:** conflict checks on the draw, match status, result entry, standings, and configurable points and tiebreakers.
- **How it is achieved:** One relational database (PostgreSQL) with transactions and constraints; scheduling and result rules enforced in the service layer; standings computed from recorded results, not stored by hand.
- **How it is checked:** Automated tests for each conflict rule; a test that recalculates standings from results; database constraints for unique and required data.

### 4.2 Security and access control

- **Why it matters:** Four kinds of users with different rights share one system. Organizers must only manage their own tournaments, referees only submit results for their own matches, and team managers only edit their own team.
- **Linked requirements:** access given by the organizer, ownership of data, audit log of changes.
- **How it is achieved:** Authentication with tokens, role-based authorization (Organizer, Referee, Team Manager, public), ownership checks on every write, passwords stored hashed, and an audit log for result and fixture changes.
- **How it is checked:** Automated tests that confirm each role can and cannot call each endpoint; tests that one organizer cannot read or change another organizer's data.

### 4.3 Modularity and maintainability

- **Why it matters:** The system has many features and is built by one person within a month. Clear boundaries keep changes local and allow the features to be built and tested one at a time.
- **Linked requirements:** the long feature list, and the optional extras (group stage, double round-robin, email invitations) that must be addable without rewriting the core.
- **How it is achieved:** A modular monolith with modules such as tournaments, teams, matches, standings, users, and news; each module has its own packages and a small public interface; the frontend is separate and talks only to the REST API.
- **How it is checked:** An automated architecture test that fails if a module depends on another module's internals.

## 5. Supporting characteristics

| Characteristic | Why it matters for Classifica | Approach | Check |
|---|---|---|---|
| Testability | Rules for scheduling, standings, and permissions must be provable | Business rules in plain classes separate from the web layer | Unit and integration tests |
| Performance (public reads) | Many visitors read scores while few people write | Indexed queries by date and tournament; pagination | Response time target for the scores endpoint, to be measured |
| Deployability | The whole system must start easily for demos and grading | Docker and Docker Compose for database, API, and frontend | `docker compose up` starts everything |
| Usability | The public site is the face of the project | Responsive layout, dark and light theme, language switch | Manual review on desktop and phone |
| Auditability | Results can be corrected, so changes must be traceable | Audit log of who changed what and when | Test that corrections create log entries |

## 6. Explicit non-goals

These are not priorities, and the design does not try to maximize them:

- **Horizontal scalability and high availability:** one application instance is enough for the expected load.
- **Real-time push at large scale:** live scores use simple polling at first.
- **Multi-region deployment.**

## 7. Trade-offs

- **Modular monolith vs microservices:** a single deployable unit is simpler to build, test, and run for one developer. The price is that modules cannot scale independently. This is accepted because of the non-goals above.
- **Consistency vs availability:** a single database with transactions favors consistency, which matches the top driving characteristic.
- **Computing standings from results vs storing them:** computing keeps standings always correct. The price is extra work on each read, which indexing and caching can reduce if needed.

## 8. Assumptions and open points

- The first version supports English only; translations are structured so another language can be added later.
- Performance targets will be set after the first measurements.
- Any technology requirement from the course will be added to the decision records.
