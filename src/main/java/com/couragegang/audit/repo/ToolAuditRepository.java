package com.couragegang.audit.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;

@Singleton
public final class ToolAuditRepository {

    private final DataSource dataSource;
    private final ObjectMapper json;

    public ToolAuditRepository(DataSource dataSource, ObjectMapper json) {
        this.dataSource = dataSource;
        this.json = json;
    }

    public UUID insert(
            UUID orgId,
            UUID workspaceId,
            UUID installationId,
            String eventType,
            String toolName,
            String outcome,
            UUID actorUserId,
            Map<String, Object> metadata)
            throws SQLException {
        try (var c = dataSource.getConnection();
                var ps = c.prepareStatement(
                        """
                        INSERT INTO tool_audit_events
                          (org_id, workspace_id, installation_id, event_type, tool_name, outcome,
                           actor_user_id, metadata)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?::jsonb)
                        RETURNING id
                        """)) {
            ps.setObject(1, orgId);
            if (workspaceId == null) {
                ps.setNull(2, Types.OTHER);
            } else {
                ps.setObject(2, workspaceId);
            }
            if (installationId == null) {
                ps.setNull(3, Types.OTHER);
            } else {
                ps.setObject(3, installationId);
            }
            ps.setString(4, eventType);
            ps.setString(5, toolName);
            ps.setString(6, outcome);
            if (actorUserId == null) {
                ps.setNull(7, Types.OTHER);
            } else {
                ps.setObject(7, actorUserId);
            }
            ps.setString(8, metadata == null ? "{}" : json.writeValueAsString(metadata));
            try (var rs = ps.executeQuery()) {
                rs.next();
                return rs.getObject(1, UUID.class);
            }
        }
    }
}
