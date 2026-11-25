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

## Tournaments
| Method | Path | Description | Success |
|---|---|---|---|
| GET | /api/tournaments | List tournaments | 200 |
| GET | /api/tournaments/{id} | Get one tournament | 200 |
| POST | /api/tournaments | Create a tournament (starts as DRAFT, unpublished) | 201 |
| PUT | /api/tournaments/{id} | Replace a tournament | 200 |
| PATCH | /api/tournaments/{id}/status | Move to the next status | 200 |
| POST | /api/tournaments/{id}/publish | Make visible to the public | 200 |
| POST | /api/tournaments/{id}/unpublish | Hide from the public | 200 |
| DELETE | /api/tournaments/{id} | Delete (draft only) | 204 |

Lifecycle: DRAFT -> REGISTRATION -> IN_PROGRESS -> COMPLETED, one step at a time (409 Conflict otherwise).
Rules: a draft tournament cannot be published; format and points cannot change once IN_PROGRESS; only drafts can be deleted.

## Tournament entries (teams in a tournament)
| Method | Path | Description | Success |
|---|---|---|---|
| GET | /api/tournaments/{tournamentId}/teams | List the teams entered | 200 |
| POST | /api/tournaments/{tournamentId}/teams | Enter a team (body: teamId, optional seed) | 201 |
| DELETE | /api/tournaments/{tournamentId}/teams/{entryId} | Remove an entry | 204 |

Rules: A team can only be entered in a tournament if it is not in another tournament that has not finished (409 Conflict otherwise). A team that is entered in a tournament cannot be deleted (409 Conflict).