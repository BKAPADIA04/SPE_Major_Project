package com.example.emergency.service;

import com.example.emergency.dto.CreateEmergencyRequest;
import com.example.emergency.model.EmergencyRequest;
import com.example.emergency.model.EmergencyStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmergencyService {

    private final Map<UUID, EmergencyRequest> emergencies = new ConcurrentHashMap<>();
    private final MLflowPriceService priceService;   // NEW

    // Inject MLflowPriceService
    public EmergencyService(MLflowPriceService priceService) {
        this.priceService = priceService;
    }

    public EmergencyRequest createEmergency(CreateEmergencyRequest dto) {
        UUID id = UUID.randomUUID();

        // Create emergency request using new constructor fields
        EmergencyRequest req = new EmergencyRequest(
                id,
                dto.getPatientName(),
                dto.getPlateNumber(),      // NEW
                dto.getLatitude(),
                dto.getLongitude(),
                EmergencyStatus.PENDING,
                Instant.now()
        );

        // Get predicted price from MLflow model
        double predictedPrice = priceService.predictPrice(
                dto.getPlateNumber(),
                dto.getLatitude(),
                dto.getLongitude()
        );
        req.setPrice(predictedPrice);       // NEW

        emergencies.put(id, req);
        return req;
    }

    public Optional<EmergencyRequest> getEmergency(UUID id) {
        return Optional.ofNullable(emergencies.get(id));
    }

    public List<EmergencyRequest> getAll() {
        return new ArrayList<>(emergencies.values());
    }

    public void updateStatus(UUID id, EmergencyStatus status) {
        EmergencyRequest req = emergencies.get(id);
        if (req != null) {
            req.setStatus(status);
        }
    }
}
