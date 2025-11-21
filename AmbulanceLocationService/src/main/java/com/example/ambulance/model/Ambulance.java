package com.example.ambulance.model;

import java.util.UUID;

public class Ambulance {

    private UUID id;
    private String plateNumber;
    private double latitude;
    private double longitude;
    private AmbulanceStatus status;

    public Ambulance() {
    }

    public Ambulance(UUID id, String plateNumber, double latitude, double longitude, AmbulanceStatus status) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public AmbulanceStatus getStatus() {
        return status;
    }

    public void setStatus(AmbulanceStatus status) {
        this.status = status;
    }
}
