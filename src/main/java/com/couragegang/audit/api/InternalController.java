package com.couragegang.audit.api;

import com.couragegang.audit.api.dto.AuditModels.IngestToolEventRequest;
import com.couragegang.audit.api.dto.AuditModels.IngestToolEventResponse;
import com.couragegang.audit.service.ToolEventIngestService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.http.HttpStatus;
import jakarta.validation.Valid;

@Controller("/internal")
public final class InternalController {

    private final ToolEventIngestService ingest;

    public InternalController(ToolEventIngestService ingest) {
        this.ingest = ingest;
    }

    @Post("/tool-events")
    @Status(HttpStatus.ACCEPTED)
    public HttpResponse<IngestToolEventResponse> ingestToolEvent(@Body @Valid IngestToolEventRequest body) {
        return HttpResponse.accepted(ingest.ingest(body));
    }
}
