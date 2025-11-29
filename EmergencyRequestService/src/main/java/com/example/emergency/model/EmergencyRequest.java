package com.example.emergency.model;

import java.time.Instant;
import java.util.UUID;

public class EmergencyRequest {

    private UUID id;
    private String patientName;
    private String plateNumber;     // NEW FIELD
    private double latitude;
    private double longitude;
    private EmergencyStatus status;
    private Instant createdAt;
    private double price;           // NEW FIELD

    public EmergencyRequest(UUID id, String patientName, String plateNumber,
                            double latitude, double longitude,
                            EmergencyStatus status, Instant createdAt) {
        this.id = id;
        this.patientName = patientName;
        this.plateNumber = plateNumber;   // NEW
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.createdAt = createdAt;
    }

    public EmergencyRequest() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public EmergencyStatus getStatus() {
        return status;
    }

    public void setStatus(EmergencyStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
