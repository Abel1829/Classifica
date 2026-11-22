# Classifica

Sports tournament management system: tournaments, fixtures, results and standings, with a public site in the style of a live-scores app.

## Architecture
React web app -> Spring Boot REST API (with Apache Camel routes) -> PostgreSQL

- **Public site:** scores by date grouped by competition, news, search, favourites, language and theme switch.
- **Admin area:** organizers manage tournaments, teams, players, fixtures, results and news.

## Tech stack
- Backend: Java 21, Spring Boot 4, Gradle
- Database: PostgreSQL (Docker)
- Frontend: React with Vite (planned)
- Integration: Apache Camel (planned)
- Containers: Docker and Docker Compose

## Run locally
1. Start the database: `docker compose up -d db`
2. Run the API: `.\gradlew bootRun`
3. The API listens on http://localhost:8081

The database is published on port 5433 and the API uses port 8081 to avoid clashes with other services on the development machine.

## Status
Project skeleton done (Gradle, Spring Boot, PostgreSQL in Docker). Next: domain model and the first API slice.