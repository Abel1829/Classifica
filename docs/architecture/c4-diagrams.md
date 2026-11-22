# C4 Diagrams

**Project:** Classifica
**Status:** Draft v1 (level 1 and level 2; the component diagram comes after the domain model)

The diagrams are written in Mermaid, so GitHub draws them directly from this file. Dashed arrows are optional or planned features.

## Level 1: System context

Who uses Classifica and which other systems it talks to.

```mermaid
flowchart TB
    public["<b>Public visitor</b><br/>[Person]<br/>Follows scores, standings,<br/>teams, players and news"]
    organizer["<b>Organizer</b><br/>[Person]<br/>Creates tournaments and venues,<br/>manages users, draws matches,<br/>publishes news"]
    referee["<b>Referee</b><br/>[Person]<br/>Records match events<br/>and submits results"]
    manager["<b>Team manager</b><br/>[Person]<br/>Manages team, players<br/>and line-ups"]

    classifica["<b>Classifica</b><br/>[Software system]<br/>Tournament management and<br/>live-scores website"]

    email["<b>Email service</b><br/>[External system]<br/>Sends invitations<br/>to new users"]

    public -->|"Views scores, standings, news"| classifica
    organizer -->|"Configures tournaments, draws matches"| classifica
    referee -->|"Submits results"| classifica
    manager -->|"Manages team and line-ups"| classifica
    classifica -.->|"Sends invitation emails (optional)"| email

    classDef person fill:#0B7A4F,stroke:#0B1F2A,color:#ffffff
    classDef system fill:#0B1F2A,stroke:#0B1F2A,color:#ffffff
    classDef external fill:#9aa5ad,stroke:#0B1F2A,color:#0B1F2A
    class public,organizer,referee,manager person
    class classifica system
    class email external
```

| Element | Responsibility |
|---|---|
| Public visitor | Uses the site without logging in |
| Organizer | Main actor: owns tournaments and gives access to referees and team managers |
| Referee | Enters match events and results for assigned matches |
| Team manager | Maintains own team, players, and match line-ups |
| Email service | External service used to send invitations (extra feature) |

## Level 2: Containers

The deployable parts of Classifica and how they communicate. All three containers run together with Docker Compose.

```mermaid
flowchart TB
    users["<b>All users</b><br/>[Person]<br/>Public, organizers,<br/>referees, team managers"]
    email["<b>Email service</b><br/>[External system]"]

    subgraph dc["Docker Compose"]
        web["<b>Web application</b><br/>[Container: React single-page app<br/>served by a static web server]<br/>Public site and admin screens"]
        api["<b>API application</b><br/>[Container: Java 21, Spring Boot]<br/>Modular monolith with REST API,<br/>security and Apache Camel routes"]
        db[("<b>Database</b><br/>[Container: PostgreSQL]<br/>Tournaments, teams, matches,<br/>results, users, news, audit log")]
    end

    users -->|"Uses in a browser (HTTPS)"| web
    web -->|"REST calls, JSON (HTTP), token in header"| api
    api -->|"Reads and writes (JDBC)"| db
    api -.->|"Sends invitation emails (SMTP, optional)"| email

    classDef person fill:#0B7A4F,stroke:#0B1F2A,color:#ffffff
    classDef container fill:#14B87A,stroke:#0B1F2A,color:#0B1F2A
    classDef store fill:#F2B01E,stroke:#0B1F2A,color:#0B1F2A
    classDef external fill:#9aa5ad,stroke:#0B1F2A,color:#0B1F2A
    class users person
    class web,api container
    class db store
    class email external
```

| Container | Technology | Responsibility |
|---|---|---|
| Web application | React with Vite | Public site (scores, standings, news, search, favourites, language, theme) and admin screens for organizers, referees, and team managers |
| API application | Spring Boot, Gradle, Apache Camel | All business rules (draw, conflict checks, standings), authentication and authorization, REST API, integration routes |
| Database | PostgreSQL | Single source of truth for all data |

### Notes

- The web application never talks to the database. Every rule is enforced in the API, which is what keeps data consistent and secure (see the architecture characteristics).
- Favourites and the chosen theme and language are stored in the visitor's browser, so they do not appear in the database.
- The API is the only part that talks to external systems.
- Decision behind this structure: `docs/decisions/0001-architectural-style.md`.
