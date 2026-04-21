package com.university.api.resource;

import com.university.api.exception.RoomNotEmptyException;
import com.university.api.model.Room;
import com.university.api.storage.DataStore;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoomResource {

    @GET
    public Response getAllRooms() {
        // Return all rooms currently stored in memory as a JSON array.
        List<Room> rooms = new ArrayList<>(DataStore.rooms.values());
        return Response.ok(rooms).build();
    }

    @POST
    public Response createRoom(Room room) {
        // Check that a request body was actually sent.
        if (room == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Room payload is required\"}")
                    .build();
        }

        // Validate the room ID.
        if (room.getId() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Room id is required\"}")
                    .build();
        }

        // Validate the room name.
        if (room.getName() == null || room.getName().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Room name is required\"}")
                    .build();
        }

        // Capacity must be greater than zero.
        if (room.getCapacity() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Room capacity must be greater than 0\"}")
                    .build();
        }

        // Prevent duplicate room IDs.
        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"A room with this id already exists\"}")
                    .build();
        }

        // Make sure sensorIds is never null.
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        // Store the room in the in-memory data store.
        DataStore.rooms.put(room.getId(), room);

        // Return 201 Created with the newly created room.
        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        // Find the room by its ID.
        Room room = DataStore.rooms.get(roomId);

        // Return 404 if the room does not exist.
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Room not found\"}")
                    .build();
        }

        // Return the room if found.
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        // Check whether the room exists before trying to delete it.
        Room room = DataStore.rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Room not found\"}")
                    .build();
        }

        // Business rule:
        // A room cannot be deleted if it still has sensors assigned to it.
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room cannot be deleted because it still has assigned sensors.");
        }

        // Remove the room from the in-memory store.
        DataStore.rooms.remove(roomId);

        // 204 No Content is the correct response for a successful DELETE.
        return Response.noContent().build();
    }
}