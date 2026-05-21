package com.couragegang.audit.api;

import com.couragegang.audit.api.dto.AuditModels.ToolEventListResponse;
import com.couragegang.audit.service.ToolEventQueryService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import jakarta.annotation.Nullable;
import java.util.UUID;

@Controller("/orgs")
public class OrgsController {

    private final ToolEventQueryService query;

    public OrgsController(ToolEventQueryService query) {
        this.query = query;
    }

    @Get("/{orgId}/tool-events")
    public ToolEventListResponse listToolEvents(
            @PathVariable UUID orgId,
            @QueryValue("workspace_id") @Nullable UUID workspaceId,
            @QueryValue(defaultValue = "50") int limit) {
        return query.list(orgId, workspaceId, limit);
    }
}
