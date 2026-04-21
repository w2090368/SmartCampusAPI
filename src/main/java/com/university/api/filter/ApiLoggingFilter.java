package com.university.api.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
public class ApiLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    // Logger instance
    private static final Logger LOGGER = Logger.getLogger(ApiLoggingFilter.class.getName());

    // Runs BEFORE the request reaches your resource
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOGGER.info("Incoming Request -> Method: "
                + requestContext.getMethod()
                + ", URI: "
                + requestContext.getUriInfo().getRequestUri());
    }

    // Runs AFTER the response is generated
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        LOGGER.info("Outgoing Response -> Status: "
                + responseContext.getStatus());
    }
}