package com.example.dispatch.dto;

import java.util.UUID;

public class DispatchResponse {

    private UUID ambulanceId;
    private String plateNumber;
    private double distanceKm;

    public DispatchResponse(UUID ambulanceId, String plateNumber, double distanceKm) {
        this.ambulanceId = ambulanceId;
        this.plateNumber = plateNumber;
        this.distanceKm = distanceKm;
    }

    public UUID getAmbulanceId() {
        return ambulanceId;
    }

    public void setAmbulanceId(UUID ambulanceId) {
        this.ambulanceId = ambulanceId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }
}
