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