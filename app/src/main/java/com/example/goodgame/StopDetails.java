package com.example.goodgame;


import com.google.android.gms.maps.model.LatLng;


public class StopDetails {


    private String name, description;
    private Boolean isFreeZone;
    private LatLng position;
    private int id;
    String distance;

    public StopDetails() {

    }

    public StopDetails(String name, String description, Boolean isFreeZone, LatLng position, int id) {
        this.name = name;
        this.description = description;
        this.isFreeZone = isFreeZone;
        this.position = position;
        this.id = id;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getDistance() { return distance; }

    public int getId() { return id; }
}