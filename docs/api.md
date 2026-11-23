# API

Base URL: http://localhost:8081

## Venues
| Method | Path | Description | Success |
|---|---|---|---|
| GET | /api/venues | List all venues | 200 |
| GET | /api/venues/{id} | Get one venue | 200 |
| POST | /api/venues | Create a venue | 201 with Location header |
| PUT | /api/venues/{id} | Replace a venue | 200 |
| DELETE | /api/venues/{id} | Delete a venue | 204 |

Errors use the Problem Details format (RFC 9457): 404 when a venue does not exist, 400 with a field-by-field `errors` object when validation fails.

## Teams
| Method | Path | Description | Success |
|---|---|---|---|
| GET | /api/teams | List all teams | 200 |
| GET | /api/teams/{id} | Get one team | 200 |
| POST | /api/teams | Create a team | 201 |
| PUT | /api/teams/{id} | Replace a team | 200 |
| DELETE | /api/teams/{id} | Delete a team and its players | 204 |

## Players (inside a team)
| Method | Path | Description | Success |
|---|---|---|---|
| GET | /api/teams/{teamId}/players | List players, ordered by shirt number | 200 |
| GET | /api/teams/{teamId}/players/{playerId} | Get one player | 200 |
| POST | /api/teams/{teamId}/players | Add a player | 201 |
| PUT | /api/teams/{teamId}/players/{playerId} | Replace a player | 200 |
| DELETE | /api/teams/{teamId}/players/{playerId} | Remove a player | 204 |

Rules: a shirt number (1 to 99) must be unique within a team (409 Conflict otherwise). Positions: GOALKEEPER, DEFENDER, MIDFIELDER, FORWARD.