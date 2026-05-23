package com.couragegang.audit.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.filter.ServerFilterChain;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

class InternalApiKeyFilterTest {

    @Test
    void rejectsMissingHeader() {
        var filter = new InternalApiKeyFilter("key");
        var chain = mock(ServerFilterChain.class);

        var response = Mono.from(filter.doFilter(HttpRequest.GET("/internal/events"), chain)).block();

        assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
    }

    @Test
    void rejectsWrongKey() {
        var filter = new InternalApiKeyFilter("key");
        var chain = mock(ServerFilterChain.class);
        var request = HttpRequest.GET("/internal/events").header(InternalApiKeyFilter.HEADER, "wrong");

        var response = Mono.from(filter.doFilter(request, chain)).block();

        assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
    }

    @Test
    void proceedsWhenKeyValid() {
        var filter = new InternalApiKeyFilter("key");
        var chain = mock(ServerFilterChain.class);
        when(chain.proceed(org.mockito.ArgumentMatchers.any())).thenReturn(Mono.empty());
        var request = HttpRequest.GET("/internal/events").header(InternalApiKeyFilter.HEADER, "key");

        Mono.from(filter.doFilter(request, chain)).block();

        verify(chain).proceed(request);
    }
}
