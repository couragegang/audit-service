package com.couragegang.audit.service;

import com.couragegang.audit.api.dto.AuditModels.IngestToolEventRequest;
import com.couragegang.audit.api.dto.AuditModels.IngestToolEventResponse;
import com.couragegang.audit.repo.ToolAuditRepository;
import jakarta.inject.Singleton;
import java.sql.SQLException;
import java.util.Map;

@Singleton
public final class ToolEventIngestService {

    private final ToolAuditRepository events;

    public ToolEventIngestService(ToolAuditRepository events) {
        this.events = events;
    }

    public IngestToolEventResponse ingest(IngestToolEventRequest req) {
        try {
            var meta = req.metadata() == null ? Map.<String, Object>of() : req.metadata();
            var id =
                    events.insert(
                            req.orgId(),
                            req.workspaceId(),
                            req.installationId(),
                            req.eventType(),
                            req.toolName(),
                            req.outcome(),
                            req.actorUserId(),
                            meta);
            return new IngestToolEventResponse(id);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
