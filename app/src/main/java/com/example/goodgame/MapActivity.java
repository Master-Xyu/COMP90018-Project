/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.goodgame;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This shows how to place markers on a map.
 */
public class MapActivity extends AppCompatActivity implements
        OnMarkerClickListener,
        OnInfoWindowClickListener,
        OnInfoWindowLongClickListener,
        OnInfoWindowCloseListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener, GoogleMap.OnMapClickListener {

    private StopDetails stopDetails = new StopDetails();


    private static final LatLng STOP1= new LatLng(-37.798439, 144.964270);

    private static final LatLng STOP3 = new LatLng(-37.801865, 144.963606);

    private static final LatLng STOP4 = new LatLng(-37.805391, 144.963094);
    //flinders st
    private static final LatLng STOP5 = new LatLng(-37.805391, 144.963094);

    private static final LatLng STOP7 = new LatLng(-37.807708, 144.962901);

    private static final LatLng STOP8 = new LatLng(-37.810035, 144.964371);

    private static final LatLng STOP10 = new LatLng(-37.812699, 144.965442);

    private static final LatLng STOP11 = new LatLng(-37.815932, 144.966946);

    private static final LatLng STOP13 = new LatLng(-37.817486, 144.966914);


    private Marker m1;
    private Marker m3;
    private Marker m4;
    private Marker m7;
    private Marker m8;
    private Marker m10;
    private Marker m11;
    private Marker m13;
    private Marker mSelectedMarker;

    private GoogleMap mMap;

    /**
     * Keeps track of the last selected marker (though it may no longer be selected).  This is
     * useful for refreshing the info window.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MapActivity.this, UserProfileActivity.class);
                startActivity(intent1);

            }
        });

        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MapActivity.this, MapActivity.class);
                startActivity(intent2);
            }
        });

        Button btnList = (Button) findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MapActivity.this, ListActivity.class);
                startActivity(intent3);
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add lots of markers to the map.
        addMarkersToMap();


        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        mMap.setContentDescription("Map with lots of markers.");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(STOP1)
                .include(STOP3)
                .include(STOP4)
                .include(STOP7)
                .include(STOP8)
                .include(STOP10)
                .include(STOP11)
                .include(STOP13)

                .build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }

    private void addMarkersToMap() {
        // Uses a colored icon.
        m1 = mMap.addMarker(new MarkerOptions()
                .position(STOP1)
                .title("Melbourne University")
                .snippet("Stop 1")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        // Uses a colored icon.
        m3 = mMap.addMarker(new MarkerOptions()
                .position(STOP3)
                .title("Lincoln Square")
                .snippet("Stop 3")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // Uses a colored icon.
        m4 = mMap.addMarker(new MarkerOptions()
                .position(STOP4)
                .title("Queensberry St & Swanston St")
                .snippet("Stop 4")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        // Uses a colored icon.
        m7 = mMap.addMarker(new MarkerOptions()
                .position(STOP7)
                .title("RMIT University")
                .snippet("Stop 7")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        m8 = mMap.addMarker(new MarkerOptions()
                .position(STOP8)
                .title("Melbourne Central Station")
                .snippet("Stop 8 <Free Tram Zone>")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        m10 = mMap.addMarker(new MarkerOptions()
                .position(STOP10)
                .title("Bourke Street Mall")
                .snippet("Stop 10 <Free Tram Zone>")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        m11 = mMap.addMarker(new MarkerOptions()
                .position(STOP11)
                .title("Collins St & Swanston St")
                .snippet("Stop 11 <Free Tram Zone>")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        m13 = mMap.addMarker(new MarkerOptions()
                .position(STOP13)
                .title("Flinders Street Station")
                .snippet("Stop 13")
                .snippet("<Free Tram Zone>")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        /**
        // Uses a custom icon with the info window popping out of the center of the icon.
        mSydney = mMap.addMarker(new MarkerOptions()
                .position(SYDNEY)
                .title("Sydney")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
                .infoWindowAnchor(0.5f, 0.5f));
         */
    }



    @Override
    public void onMapClick(final LatLng point) {
        // Any showing info window closes when the map is clicked.
        // Clear the currently selected marker.
        mSelectedMarker = null;
    }

    //
    // Marker related listeners.
    //
    @Override
    public boolean onMarkerClick(final Marker marker) {

        //set the bounced marker
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 2 * t);

                if (t > 0.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
        if (marker.equals(mSelectedMarker)) {
            // The showing info window has already been closed - that's the first thing to happen
            // when any marker is clicked.
            // Return true to indicate we have consumed the event and that we do not want the
            // the default behavior to occur (which is for the camera to move such that the
            // marker is centered and for the marker's info window to open, if it has one).
            mSelectedMarker = null;

            return true;
        }

        mSelectedMarker = marker;

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur.
        return false;

    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MapActivity.this, DetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        //Toast.makeText(this, "Close Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Toast.makeText(this, "Info Window long click", Toast.LENGTH_SHORT).show();
    }



}
