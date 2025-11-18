package com.example.ambulance.controller;

import com.example.ambulance.model.Ambulance;
import com.example.ambulance.model.AmbulanceStatus;
import com.example.ambulance.service.AmbulanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ambulances")
public class AmbulanceController {

    private final AmbulanceService ambulanceService;

    public AmbulanceController(AmbulanceService ambulanceService) {
        this.ambulanceService = ambulanceService;
    }

    @PostMapping
    public ResponseEntity<Ambulance> register(@RequestParam String plateNumber,
                                              @RequestParam double latitude,
                                              @RequestParam double longitude) {
        Ambulance created = ambulanceService.register(plateNumber, latitude, longitude);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public List<Ambulance> getAll() {
        return ambulanceService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ambulance> getById(@PathVariable UUID id) {
        return ambulanceService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<Ambulance> updateLocation(@PathVariable UUID id,
                                                    @RequestParam double latitude,
                                                    @RequestParam double longitude) {
        return ambulanceService.updateLocation(id, latitude, longitude)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Ambulance> updateStatus(@PathVariable UUID id,
                                                  @RequestParam AmbulanceStatus status) {
        return ambulanceService.updateStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
