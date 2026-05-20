# audit-service

Platform audit — append-only tool/installation events (`/v1/audit`).

- **Контракт:** [`../api-contracts/audit/openapi.yaml`](../api-contracts/audit/openapi.yaml)
- **Порт:** 8086

## Internal ingest

```http
POST /v1/audit/internal/tool-events
X-Audit-Internal-Key: dev-internal-key

{
  "orgId": "...",
  "workspaceId": "...",
  "installationId": "...",
  "eventType": "mcp.installation",
  "toolName": "notion",
  "outcome": "created",
  "actorUserId": "...",
  "metadata": {}
}
```

Ответ `202`: `{ "eventId": "..." }`.

## Запуск

```bash
docker compose up --build
```
