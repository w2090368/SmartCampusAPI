package com.university.api.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Sensor is unavailable.");
        errorResponse.put("message", exception.getMessage());
        errorResponse.put("status", 403);

        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorResponse)
                .build();
    }
}