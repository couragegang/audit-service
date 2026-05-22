package com.couragegang.audit.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.couragegang.audit.api.dto.AuditModels.IngestToolEventRequest;
import com.couragegang.audit.api.dto.AuditModels.IngestToolEventResponse;
import com.couragegang.audit.service.ToolEventIngestService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InternalControllerTest {

    @Mock
    ToolEventIngestService ingest;

    @InjectMocks
    InternalController controller;

    @Test
    void ingestDelegates() {
        var orgId = UUID.randomUUID();
        var eventId = UUID.randomUUID();
        var req = new IngestToolEventRequest(orgId, UUID.randomUUID(), null, "install", "notion", "ok", null, null);
        when(ingest.ingest(req)).thenReturn(new IngestToolEventResponse(eventId));

        var res = controller.ingestToolEvent(req);

        assertThat(res.eventId()).isEqualTo(eventId);
    }
}
