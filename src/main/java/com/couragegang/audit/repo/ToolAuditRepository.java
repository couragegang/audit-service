package com.couragegang.audit.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.inject.Singleton;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
            ps.setString(8, metadata == null ? "{}" : toJson(metadata));
            try (var rs = ps.executeQuery()) {
                rs.next();
                return rs.getObject(1, UUID.class);
            }
        }
    }

    public List<ToolEventRow> listByOrg(UUID orgId, UUID workspaceId, int limit) throws SQLException {
        var sql =
                """
                SELECT id, org_id, workspace_id, installation_id, event_type, tool_name, outcome,
                       actor_user_id, metadata::text, created_at
                FROM tool_audit_events
                WHERE org_id = ?
                """;
        if (workspaceId != null) {
            sql += " AND workspace_id = ?";
        }
        sql += " ORDER BY created_at DESC LIMIT ?";
        try (var c = dataSource.getConnection();
                var ps = c.prepareStatement(sql)) {
            ps.setObject(1, orgId);
            int idx = 2;
            if (workspaceId != null) {
                ps.setObject(idx++, workspaceId);
            }
            ps.setInt(idx, limit);
            var rows = new ArrayList<ToolEventRow>();
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(
                            new ToolEventRow(
                                    rs.getObject("id", UUID.class),
                                    rs.getObject("org_id", UUID.class),
                                    rs.getObject("workspace_id", UUID.class),
                                    rs.getObject("installation_id", UUID.class),
                                    rs.getString("event_type"),
                                    rs.getString("tool_name"),
                                    rs.getString("outcome"),
                                    rs.getObject("actor_user_id", UUID.class),
                                    rs.getString("metadata"),
                                    rs.getTimestamp("created_at").toInstant()));
                }
            }
            return rows;
        }
    }

    public Map<String, Object> parseMetadata(String jsonText) {
        if (jsonText == null || jsonText.isBlank()) {
            return Map.of();
        }
        try {
            return json.readValue(jsonText, new TypeReference<>() {});
        } catch (Exception e) {
            return Map.of();
        }
    }

    private String toJson(Map<String, Object> value) throws SQLException {
        try {
            return json.writeValueAsString(value);
        } catch (Exception e) {
            throw new SQLException("invalid json", e);
        }
    }

    public record ToolEventRow(
            UUID id,
            UUID orgId,
            UUID workspaceId,
            UUID installationId,
            String eventType,
            String toolName,
            String outcome,
            UUID actorUserId,
            String metadataJson,
            Instant createdAt) {}
}

