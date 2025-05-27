package org.example;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Logged
@Interceptor
public class LoggingInterceptor {

    @AroundInvoke
    public Object logMethod(InvocationContext ctx) throws Exception {
        System.out.println(">> Entering: " + ctx.getMethod().getName());
        Object result = ctx.proceed();
        System.out.println("<< Exiting: " + ctx.getMethod().getName());
        return result;
    }
}
