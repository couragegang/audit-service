package com.couragegang.audit.api;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import java.util.Map;

@Controller
public final class HealthInfoController {

    @Get("/")
    public Map<String, String> root() {
        return Map.of(
                "service", "audit-service",
                "health", "/v1/audit/health",
                "metrics", "/v1/audit/metrics",
                "internal", "/v1/audit/internal/tool-events",
                "toolEvents", "/v1/audit/orgs/{orgId}/tool-events");
    }
}
