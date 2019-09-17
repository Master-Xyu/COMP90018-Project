package com.example.goodgame;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StopDetails {

    //The id of the stop.
    private int stopId;
    //The name of the stop.
    private String stopName;
    //The coordinate of the stop.
    private LatLng coordinate;
    //The resources id of the description of the demo.
    private String description;

    private int score;

    public StopDetails() {

    }

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    {
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/posts");
    }

    public LatLng getCoordinate() {
        this.coordinate= new LatLng(-37.798439, 144.964270);
        return coordinate;
    }

    public int getScore() {
        this.score=90;
        return score;
    }
}
