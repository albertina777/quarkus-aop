package org.example;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.opentelemetry.instrumentation.annotations.WithSpan;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/test")
public class GreetingResource {

    @Inject
    MeterRegistry registry;

    /**
     * REST endpoint that returns a greeting message.
     *
     * Micrometer Observability:
     * - @Timed: Automatically tracks invocation count and timing.
     * - @WithSpan: Adds a span for distributed tracing (OpenTelemetry).
     * - @Logged: Logs method entry and exit via CDI interceptor.
     * - Gauge and DistributionSummary for additional custom metrics.
     */
    @Path("/helloCounter")
    @GET
    @Logged
    @WithSpan
    @Timed(value = "hello_method_execution_timer", extraTags = {"endpoint", "helloCounter"})
    public String hello() {

        // Increment a counter manually
        registry.counter("hello_method_invocations_total",
                "endpoint", "helloCounter").increment();

        // Simulate a gauge value (e.g., active users)
        AtomicInteger activeUsers = new AtomicInteger(new Random().nextInt(100));
        registry.gauge("hello_active_users", Tags.of("endpoint", "helloCounter"), activeUsers);

        // Record a distribution summary (e.g., payload size)
        DistributionSummary summary = DistributionSummary.builder("hello_response_size_bytes")
                .tags("endpoint", "helloCounter")
                .register(registry);
        summary.record(28); // Length of returned string in bytes

        return "Hello from Red Hat Quarkus 3.20!";
    }
}
