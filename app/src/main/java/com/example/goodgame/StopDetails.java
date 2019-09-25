package com.example.goodgame;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;


public class StopDetails {


    private String name, description;
    private Boolean isFreeZone;
    private LatLng position;

    public StopDetails() {

    }

    public StopDetails(String name, String description, Boolean isFreeZone, LatLng position) {
        this.name = name;
        this.description = description;
        this.isFreeZone = isFreeZone;
        this.position = position;
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

    public LatLng getPosition() {
        return position;
    }
}
