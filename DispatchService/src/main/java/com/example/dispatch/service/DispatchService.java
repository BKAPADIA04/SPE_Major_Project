package com.example.dispatch.service;

import com.example.dispatch.dto.DispatchRequest;
import com.example.dispatch.dto.DispatchResponse;
import com.example.dispatch.model.AmbulanceSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class DispatchService {

    private final RestTemplate restTemplate;
    private final String ambulanceServiceBaseUrl;

    public DispatchService(RestTemplate restTemplate,
                           @Value("${ambulance.service.base-url}") String ambulanceServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.ambulanceServiceBaseUrl = ambulanceServiceBaseUrl;
    }

    public Optional<DispatchResponse> findNearestAmbulance(DispatchRequest request) {
        String url = ambulanceServiceBaseUrl + "/api/ambulances";
        AmbulanceSummary[] ambulancesArray = restTemplate.getForObject(url, AmbulanceSummary[].class);
        if (ambulancesArray == null || ambulancesArray.length == 0) {
            return Optional.empty();
        }

        List<AmbulanceSummary> ambulances = Arrays.asList(ambulancesArray);

        AmbulanceSummary nearest = ambulances.stream()
                .filter(a -> "AVAILABLE".equalsIgnoreCase(a.getStatus()))
                .min(Comparator.comparingDouble(a ->
                        distanceKm(request.getEmergencyLatitude(), request.getEmergencyLongitude(),
                                a.getLatitude(), a.getLongitude())))
                .orElse(null);

        if (nearest == null) {
            return Optional.empty();
        }

        double distance = distanceKm(request.getEmergencyLatitude(), request.getEmergencyLongitude(),
                nearest.getLatitude(), nearest.getLongitude());

        return Optional.of(new DispatchResponse(nearest.getId(), nearest.getPlateNumber(), distance));
    }

    // Simple Haversine distance
    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
