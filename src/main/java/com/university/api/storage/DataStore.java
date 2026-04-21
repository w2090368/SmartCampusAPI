package com.university.api.storage;

import com.university.api.model.Room;
import com.university.api.model.Sensor;
import com.university.api.model.SensorReading;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    // Stores all rooms (Room ID -> Room object)
    public static Map<String, Room> rooms = new ConcurrentHashMap<>();

    // Stores all sensors (Sensor ID -> Sensor object)
    public static Map<String, Sensor> sensors = new ConcurrentHashMap<>();

    // Stores reading history for each sensor (Sensor ID -> List of readings)
    public static Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();
}