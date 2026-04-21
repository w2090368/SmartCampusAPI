package com.university.api.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        // Build a clean JSON error response for invalid linked resources.
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Referenced resource does not exist.");
        errorResponse.put("message", exception.getMessage());
        errorResponse.put("status", 422);

        // Return 422 Unprocessable Entity.
        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorResponse)
                .build();
    }
}