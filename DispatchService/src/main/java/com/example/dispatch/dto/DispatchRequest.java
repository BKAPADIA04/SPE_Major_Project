package com.example.dispatch.dto;

import jakarta.validation.constraints.NotNull;

public class DispatchRequest {

    @NotNull
    private Double emergencyLatitude;

    @NotNull
    private Double emergencyLongitude;

    public Double getEmergencyLatitude() {
        return emergencyLatitude;
    }

    public void setEmergencyLatitude(Double emergencyLatitude) {
        this.emergencyLatitude = emergencyLatitude;
    }

    public Double getEmergencyLongitude() {
        return emergencyLongitude;
    }

    public void setEmergencyLongitude(Double emergencyLongitude) {
        this.emergencyLongitude = emergencyLongitude;
    }
}
