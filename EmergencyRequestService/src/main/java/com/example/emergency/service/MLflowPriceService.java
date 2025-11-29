package com.example.emergency.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;

/**
 * Calls the Python MLflow model prediction service
 * to get the price/cost of sending an ambulance.
 */
@Service
public class MLflowPriceService {

    private static final String MLFLOW_URL = "http://127.0.0.1:5001/predict";

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Calls MLflow model API and returns predicted price.
     */
    public double predictPrice(String plateNumber, double latitude, double longitude) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "plateNumber", plateNumber,
                    "latitude", latitude,
                    "longitude", longitude
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(MLFLOW_URL, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object predicted = response.getBody().get("predicted_price");
                return predicted != null ? Double.parseDouble(predicted.toString()) : 0.0;
            }

            return 0.0;

        } catch (Exception e) {
            System.out.println("MLflow prediction failed: " + e.getMessage());
            return 0.0;  // fallback
        }
    }
}
