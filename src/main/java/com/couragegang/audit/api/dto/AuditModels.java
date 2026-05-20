package com.couragegang.audit.api.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

public final class AuditModels {

    private AuditModels() {}

    @Serdeable
    public record IngestToolEventRequest(
            @NotNull UUID orgId,
            UUID workspaceId,
            UUID installationId,
            @NotNull String eventType,
            @NotNull String toolName,
            @NotNull String outcome,
            UUID actorUserId,
            Map<String, Object> metadata) {}

    @Serdeable
    public record IngestToolEventResponse(UUID eventId) {}

    @Serdeable
    public record ErrorBody(String code, String message) {
        public static ErrorBody of(String code, String message) {
            return new ErrorBody(code, message);
        }
    }
}
