package com.example.ambulance.service;

import com.example.ambulance.model.Ambulance;
import com.example.ambulance.model.AmbulanceStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AmbulanceService {

    private final Map<UUID, Ambulance> ambulances = new ConcurrentHashMap<>();

    public Ambulance register(String plateNumber, double latitude, double longitude) {
        UUID id = UUID.randomUUID();
        Ambulance ambulance = new Ambulance(id, plateNumber, latitude, longitude, AmbulanceStatus.AVAILABLE);
        ambulances.put(id, ambulance);
        return ambulance;
    }

    public List<Ambulance> getAll() {
        return new ArrayList<>(ambulances.values());
    }

    public Optional<Ambulance> getById(UUID id) {
        return Optional.ofNullable(ambulances.get(id));
    }

    public Optional<Ambulance> updateLocation(UUID id, double latitude, double longitude) {
        Ambulance amb = ambulances.get(id);
        if (amb != null) {
            amb.setLatitude(latitude);
            amb.setLongitude(longitude);
            return Optional.of(amb);
        }
        return Optional.empty();
    }

    public Optional<Ambulance> updateStatus(UUID id, AmbulanceStatus status) {
        Ambulance amb = ambulances.get(id);
        if (amb != null) {
            amb.setStatus(status);
            return Optional.of(amb);
        }
        return Optional.empty();
    }
}
