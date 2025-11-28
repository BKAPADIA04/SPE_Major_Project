package com.example.ambulance.controller;

import com.example.ambulance.model.Ambulance;
import com.example.ambulance.model.AmbulanceStatus;
import com.example.ambulance.service.AmbulanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ambulances")
public class AmbulanceController {

    private final AmbulanceService ambulanceService;
    private static final Logger logger = LoggerFactory.getLogger(AmbulanceController.class);

    public AmbulanceController(AmbulanceService ambulanceService) {
        this.ambulanceService = ambulanceService;
    }

    @PostMapping
    public ResponseEntity<Ambulance> register(@RequestParam String plateNumber,
                                              @RequestParam double latitude,
                                              @RequestParam double longitude) {
        Ambulance created = ambulanceService.register(plateNumber, latitude, longitude);
        logger.info("Received register request for plateNumber={}", plateNumber);
        System.out.println("Application has started!");
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public List<Ambulance> getAll() {
        logger.info("Received request to get all ambulances");
        System.out.println("Application has started!");
        return ambulanceService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ambulance> getById(@PathVariable UUID id) {
        logger.info("Received request to get ambulance by id={}", id);
        System.out.println("Application has started!");
        return ambulanceService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<Ambulance> updateLocation(@PathVariable UUID id,
                                                    @RequestParam double latitude,
                                                    @RequestParam double longitude) {

        logger.info("Received request to update location for ambulance id={}, latitude={}, longitude={}", id, latitude, longitude);
        System.out.println("Application has started!");
        return ambulanceService.updateLocation(id, latitude, longitude)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Ambulance> updateStatus(@PathVariable UUID id,
                                                  @RequestParam AmbulanceStatus status) {
        logger.info("Received request to update status for ambulance id={}, status={}", id, status);
        System.out.println("Application has started!");
        return ambulanceService.updateStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
