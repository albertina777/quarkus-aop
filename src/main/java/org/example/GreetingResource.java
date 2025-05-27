package org.example;


import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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
 * This method is instrumented using Micrometer:
 * - Increments a counter to track invocation count.
 * - Records execution time using a timer.
 * - Adds a tracing span for distributed tracing via OpenTelemetry.
 * - @WithSpan: Automatically creates a tracing span for distributed tracing.
 * - @Logged: Logs method entry and exit for observability.
 * - Gauge: hello_active_users – simulates a dynamic value (you can replace it with a real metric).
 * - Distribution Summary: hello_response_size_bytes – tracks the size of the response payload.
 *
 * Metrics are exposed at /q/metrics and can be scraped by Prometheus.
 */

@Path("/helloCounter")
@GET
@Logged
@WithSpan
public String hello() {

    // Increment a counter to track method invocations
    registry.counter("hello_method_invocations_total", 
        "endpoint", "helloCounter").increment();

    // Simulate a dynamic value for gauge (e.g., active users)
    AtomicInteger activeUsers = new AtomicInteger(new Random().nextInt(100));
    registry.gauge("hello_active_users", Tags.of("endpoint", "helloCounter"), activeUsers);

    // Record a distribution summary (e.g., response size)
    DistributionSummary summary = DistributionSummary
        .builder("hello_response_size_bytes")
        .tags("endpoint", "helloCounter")
        .register(registry);
    summary.record(28); // "Hello from Red Hat Quarkus 3.20!" is 28 bytes

    // Record execution time using a timer
    return registry.timer("hello_method_execution_timer", 
        "endpoint", "helloCounter").record(() -> {
            return "Hello from Red Hat Quarkus 3.20!";
        });
}


}

