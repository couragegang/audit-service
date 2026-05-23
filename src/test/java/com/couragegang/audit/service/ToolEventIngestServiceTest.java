package com.couragegang.audit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.couragegang.audit.api.dto.AuditModels.IngestToolEventRequest;
import com.couragegang.audit.repo.ToolAuditRepository;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ToolEventIngestServiceTest {

    @Mock
    ToolAuditRepository events;

    ToolEventIngestService svc;
    UUID orgId = UUID.randomUUID();
    UUID wsId = UUID.randomUUID();
    UUID eventId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        svc = new ToolEventIngestService(events);
    }

    @Test
    void ingestStoresEvent() throws Exception {
        when(events.insert(eq(orgId), eq(wsId), any(), eq("install"), eq("notion"), eq("ok"), any(), any()))
                .thenReturn(eventId);

        var res =
                svc.ingest(
                        new IngestToolEventRequest(
                                orgId, wsId, UUID.randomUUID(), "install", "notion", "ok", UUID.randomUUID(), null));

        assertThat(res.eventId()).isEqualTo(eventId);
    }

    @Test
    void ingestUsesEmptyMetadataWhenNull() throws Exception {
        when(events.insert(any(), any(), any(), any(), any(), any(), any(), eq(Map.of()))).thenReturn(eventId);

        svc.ingest(
                new IngestToolEventRequest(
                        orgId, wsId, null, "tool_call", "t", "ok", null, null));

        assertThat(eventId).isNotNull();
    }

    @Test
    void ingestWrapsSqlException() throws Exception {
        when(events.insert(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new SQLException("db"));

        assertThatThrownBy(
                        () ->
                                svc.ingest(
                                        new IngestToolEventRequest(
                                                orgId, wsId, null, "install", "n", "ok", null, Map.of())))
                .isInstanceOf(IllegalStateException.class);
    }
}
