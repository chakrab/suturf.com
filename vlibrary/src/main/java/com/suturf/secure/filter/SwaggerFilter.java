package com.suturf.secure.filter;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.cookie.Cookie;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;

import java.time.Duration;

@Requires(property = "micronaut.server.context-path")
@Filter(methods = {HttpMethod.GET, HttpMethod.HEAD}, patterns = {"/**/swagger-ui*"})
public class SwaggerFilter implements HttpServerFilter {

    private final Cookie contextPathCookie;
    public SwaggerFilter(@Value("${micronaut.server.context-path}") final String contextPath) {
        this.contextPathCookie = Cookie.of("contextPath", contextPath)
                .maxAge(Duration.ofMinutes(2L));
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(
            final HttpRequest<?> request, final ServerFilterChain chain) {
        return Publishers.map(chain.proceed(request),
                response -> response.cookie(contextPathCookie));
    }
}
