package com.university.api.resource;

import com.university.api.exception.LinkedResourceNotFoundException;
import com.university.api.model.Room;
import com.university.api.model.Sensor;
import com.university.api.storage.DataStore;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        // Start with all sensors stored in memory.
        List<Sensor> allSensors = new ArrayList<>(DataStore.sensors.values());

        // If no type query parameter is provided, return the full list.
        if (type == null || type.trim().isEmpty()) {
            return Response.ok(allSensors).build();
        }

        // Otherwise, filter sensors by matching type.
        List<Sensor> filteredSensors = new ArrayList<>();
        for (Sensor sensor : allSensors) {
            if (sensor.getType() != null && sensor.getType().equalsIgnoreCase(type)) {
                filteredSensors.add(sensor);
            }
        }

        return Response.ok(filteredSensors).build();
    }

    @POST
    public Response createSensor(Sensor sensor) {
        // Check that a request body was provided.
        if (sensor == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Sensor payload is required\"}")
                    .build();
        }

        // Validate sensor id.
        if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Sensor id is required\"}")
                    .build();
        }

        // Validate sensor type.
        if (sensor.getType() == null || sensor.getType().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Sensor type is required\"}")
                    .build();
        }

        // Validate sensor status.
        if (sensor.getStatus() == null || sensor.getStatus().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Sensor status is required\"}")
                    .build();
        }

        // Validate roomId.
        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"roomId is required\"}")
                    .build();
        }

        // Prevent duplicate sensor IDs.
        if (DataStore.sensors.containsKey(sensor.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"A sensor with this id already exists\"}")
                    .build();
        }

        // Check that the linked room actually exists.
        Room room = DataStore.rooms.get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException(
                    "The specified roomId does not exist: " + sensor.getRoomId()
            );
        }

        // Save the sensor in memory.
        DataStore.sensors.put(sensor.getId(), sensor);

        // Also link the sensor back to the room to maintain consistency.
        room.getSensorIds().add(sensor.getId());

        // Return 201 Created with the created sensor.
        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}