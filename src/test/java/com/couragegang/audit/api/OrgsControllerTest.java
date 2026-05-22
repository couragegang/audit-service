package com.couragegang.audit.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.couragegang.audit.api.dto.AuditModels.ToolEventListResponse;
import com.couragegang.audit.service.ToolEventQueryService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrgsControllerTest {

    @Mock
    ToolEventQueryService query;

    @InjectMocks
    OrgsController controller;

    @Test
    void listToolEventsDelegates() {
        var orgId = UUID.randomUUID();
        var wsId = UUID.randomUUID();
        var expected = new ToolEventListResponse(List.of());
        when(query.list(orgId, wsId, 25)).thenReturn(expected);

        var res = controller.listToolEvents(orgId, wsId, 25);

        assertThat(res).isSameAs(expected);
    }
}
