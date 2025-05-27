package org.example;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import io.opentelemetry.instrumentation.annotations.WithSpan;

@Path("/test")
public class GreetingResource {

    /**
 * REST endpoint that returns a greeting message.
 * 
 * This method is instrumented with:
 * - @Counted: Tracks how many times the endpoint is called (helloCount).
 * - @Timed: Measures execution time of the method (helloTimer).
 * - @WithSpan: Automatically creates a tracing span for distributed tracing.
 * - @Logged: Logs method entry and exit for observability.
 * 
 * Metrics are exposed at /q/metrics and traces are exported via OpenTelemetry.
 */

    @Path("/helloCounter")
    @GET
    @Logged
    @WithSpan
    @Counted(name = "helloCount", description = "How many times hello was called")
    @Timed(name = "helloTimer", description = "Time taken by hello method")
    public String hello() {
        return "Hello from Red Hat Quarkus 3.20!";
    }

}

