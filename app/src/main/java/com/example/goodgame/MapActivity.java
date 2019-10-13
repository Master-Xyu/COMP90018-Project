package com.example.goodgame;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This shows how to place markers on a map.
 */
public class MapActivity extends AppCompatActivity implements
        OnMarkerClickListener,
        OnInfoWindowClickListener,
        OnInfoWindowLongClickListener,
        OnInfoWindowCloseListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener,
        GoogleMap.OnMapClickListener {

    String TAG = "MapActivity";
    private FirebaseFirestore db;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private static LatLng myLocation;

    private int[]score={20,30,40,50,60,70,80,90};

    private Marker mSelectedMarker;

    private GoogleMap mMap;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        /*
        db.collection("stopInfo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d : list) {
                                StopDetails s = d.toObject(StopDetails.class);
                                stopList.add(s);
                            }
                        }
                    }
                });

         */
        /*
        db.collection("stopInfo").get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                StopDetails s = document.toObject(StopDetails.class);
                                stopList.add(s);
                            }
                            System.out.println("&&&&&&&&&&&&&&&&&");
                            System.out.println(stopList.size());
                            System.out.println(stopList.get(0).getFreeZone());
                            System.out.println("&&&&&&&&&&&&&&&&&");
                        }
                    }
                });

         */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setTitle("");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getPhotoUrl() != null){
            BaseApplication.changeImage(user.getPhotoUrl());
        }

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

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myLocation = new LatLng(location.getLatitude(),location.getLongitude());
                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(myLocation)
                        .build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 15));

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent4 = new Intent(MapActivity.this, DetailActivity.class);
                startActivity(intent4);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 10);
                return;
            }
        }
        locationManager.requestLocationUpdates("gps", 10000, 0, locationListener);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Hide the zoom controls as the button will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);
        mMap.setMyLocationEnabled(true);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        mMap.setContentDescription("Map with lots of markers.");

        addMarkersToMap();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.menuSearch);

        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void addMarkersToMap() {
        //if (name.size()==0 )
        //    return;
        for (int i=0; i<StopDetailsList.STOPS.length;i++){
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(StopDetailsList.STOPS[i].getPosition())
                    .title(StopDetailsList.STOPS[i].getName())
                    .snippet(StopDetailsList.STOPS[i].getDescription())
                    //.title(stopList.get(i).getName())
                    //.snippet(stopList.get(i).getDescription())
                    .icon(getIcon(score[i])));
                    //.infoWindowAnchor(0.5f, 0.5f));
        }
    }


    public BitmapDescriptor getIcon(int score){
        if (score<=30){
            //safe

            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)) //using custom icon
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        }
        else if(score>30 && score<80){
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        }
        else{
            //dangerous
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        }
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
        intent.putExtra("stopID",marker.getId()+1);
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
