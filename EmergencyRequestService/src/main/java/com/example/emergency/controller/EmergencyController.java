package com.example.emergency.controller;

import com.example.emergency.dto.CreateEmergencyRequest;
import com.example.emergency.model.EmergencyRequest;
import com.example.emergency.service.EmergencyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/emergencies")
public class EmergencyController {

    private final EmergencyService emergencyService;

    public EmergencyController(EmergencyService emergencyService) {
        this.emergencyService = emergencyService; 
    }

    @PostMapping
    public ResponseEntity<EmergencyRequest> create(@Valid @RequestBody CreateEmergencyRequest request) {
        EmergencyRequest created = emergencyService.createEmergency(request);
        System.out.println("Created emergency request: " + created);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyRequest> getById(@PathVariable UUID id) {
        System.out.println("Fetching emergency request with ID: " + id);
        return emergencyService.getEmergency(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<EmergencyRequest> getAll() {
        System.out.println("Fetching all emergency requests");
        return emergencyService.getAll();
    }
}
