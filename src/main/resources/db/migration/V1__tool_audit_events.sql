CREATE TABLE tool_audit_events (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id           UUID NOT NULL,
    workspace_id     UUID,
    installation_id  UUID,
    event_type       TEXT NOT NULL,
    tool_name        TEXT NOT NULL,
    outcome          TEXT NOT NULL,
    actor_user_id    UUID,
    metadata         JSONB NOT NULL DEFAULT '{}',
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX tool_audit_events_org_created_idx ON tool_audit_events (org_id, created_at DESC);
