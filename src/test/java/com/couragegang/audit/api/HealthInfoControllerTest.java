package com.couragegang.audit.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class HealthInfoControllerTest {

    @Test
    void rootMapsService() {
        assertThat(new HealthInfoController().root().get("service")).isEqualTo("audit-service");
    }
}
