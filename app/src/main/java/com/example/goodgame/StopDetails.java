package com.example.goodgame;


import com.google.android.gms.maps.model.LatLng;


public class StopDetails {


    private String name, description;
    private Boolean isFreeZone;

    public StopDetails() {

    }

    public StopDetails(String name, String description, Boolean isFreeZone) {
        this.name = name;
        this.description = description;
        this.isFreeZone = isFreeZone;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getFreeZone() {
        return isFreeZone;
    }


}
