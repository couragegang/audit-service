package com.couragegang.audit.service;

import com.couragegang.audit.api.dto.AuditModels.ToolEventListResponse;
import com.couragegang.audit.api.dto.AuditModels.ToolEventView;
import com.couragegang.audit.repo.ToolAuditRepository;
import jakarta.inject.Singleton;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

@Singleton
public final class ToolEventQueryService {

    private final ToolAuditRepository events;

    public ToolEventQueryService(ToolAuditRepository events) {
        this.events = events;
    }

    public ToolEventListResponse list(UUID orgId, UUID workspaceId, int limit) {
        try {
            var rows = events.listByOrg(orgId, workspaceId, limit);
            var items = new ArrayList<ToolEventView>();
            for (var row : rows) {
                items.add(
                        new ToolEventView(
                                row.id(),
                                row.orgId(),
                                row.workspaceId(),
                                row.installationId(),
                                row.eventType(),
                                row.toolName(),
                                row.outcome(),
                                row.actorUserId(),
                                events.parseMetadata(row.metadataJson()),
                                row.createdAt()));
            }
            return new ToolEventListResponse(items);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
