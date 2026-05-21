package com.couragegang.audit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.couragegang.audit.repo.ToolAuditRepository;
import com.couragegang.audit.repo.ToolAuditRepository.ToolEventRow;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ToolEventQueryServiceTest {

    @Mock
    ToolAuditRepository events;

    ToolEventQueryService svc;
    UUID orgId = UUID.randomUUID();
    UUID wsId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        svc = new ToolEventQueryService(events);
    }

    @Test
    void listMapsRows() throws Exception {
        var id = UUID.randomUUID();
        when(events.listByOrg(orgId, wsId, 10))
                .thenReturn(
                        List.of(
                                new ToolEventRow(
                                        id,
                                        orgId,
                                        wsId,
                                        UUID.randomUUID(),
                                        "install",
                                        "notion",
                                        "ok",
                                        UUID.randomUUID(),
                                        "{}",
                                        Instant.parse("2026-01-01T00:00:00Z"))));
        when(events.parseMetadata("{}")).thenReturn(Map.of("k", "v"));

        var page = svc.list(orgId, wsId, 10);

        assertThat(page.items()).hasSize(1);
        assertThat(page.items().getFirst().metadata()).containsEntry("k", "v");
    }
}
