package com.university.api.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        // Create a clean JSON error response instead of exposing raw server errors.
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Room is currently occupied by active hardware.");
        errorResponse.put("message", exception.getMessage());
        errorResponse.put("status", 409);

        // Return HTTP 409 Conflict with a JSON body.
        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorResponse)
                .build();
    }
}