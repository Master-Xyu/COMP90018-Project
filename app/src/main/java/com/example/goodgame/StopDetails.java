package com.example.goodgame;


import android.content.Intent;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;


public class StopDetails {


    private String name, description;
    private Boolean isFreeZone;
    private LatLng position;
    FirebaseAuth firebaseAuth;

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

    /*handle menu item clicks*/
    public  boolean onOptionsItemSelected(MenuItem item){
        int id =item.getItemId();
        if(id==R.id.action_add_post) {
        startActivity(new Intent(getActivity(),AddPostActivity.class))
        }

    }

    public  void checkUserStatus(){
        //get current user
        FirebaseUser user =firebaseAuth.getCurrentUser();
        if(user!=null){
            //???
            //
            //
        }
        else{
            startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
        }
    }
}
