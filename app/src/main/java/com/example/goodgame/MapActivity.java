package com.example.goodgame;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.List;

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

    private int[]score={20,30,40,50,60,70,80,90,20,30,40,50,60,70,80,90,70,80,90};

    private List<Marker> mMarker;

    private Marker mSelectedMarker;

    private GoogleMap mMap;

    private SearchView searchView;

    private AutoCompleteTextView mSearchText;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setTitle("");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Uri u = user.getPhotoUrl();
        if(u != null){
            BaseApplication.changeImage(u);
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


        Button btnGame = (Button) findViewById(R.id.gamebtn);
        btnGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, StartGameActivity.class);
                startActivity(intent);
            }
        });





        mSearchText = (AutoCompleteTextView) findViewById(R.id.search_input1);
        String[] name = new String[StopDetailsList.STOPS.length];
        for (int i=0; i<name.length;i++){
            name[i] = StopDetailsList.STOPS[i].getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,name);
        mSearchText.setAdapter(adapter);
        mSearchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String location = mSearchText.getText().toString();
                for(int j=0; j<mMarker.size();j++){
                    if(mMarker.get(j).getTitle().equals(location)){
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarker.get(j).getPosition(),15));
                        markerBounce(mMarker.get(j));
                    }
                }
            }
        });
/*
        ImageButton btnReturn = (ImageButton)findViewById(R.id.ic_return);
        btnReturn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String location = mSearchText.getText().toString();
                for(int j=0; j<mMarker.size();j++){
                    if(mMarker.get(j).getTitle().equals(location)){
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarker.get(j).getPosition(),15));
                        markerBounce(mMarker.get(j));
                        closeKeyboard();
                    }
                }
            }
        });

        ImageButton btnClear = (ImageButton)findViewById(R.id.ic_clear);
        btnClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mSearchText.setText("");
                closeKeyboard();
            }
        });


 */


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
        mMap.getUiSettings().setZoomControlsEnabled(true);

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

    private void initSearch(){
        String[] name = new String[StopDetailsList.STOPS.length];
        for (int i=0; i<name.length;i++){
            name[i] = StopDetailsList.STOPS[i].getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,name);
        mSearchText.setAdapter(adapter);
        mSearchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String location = mSearchText.getText().toString();
                for(int j=0; j<mMarker.size();j++){
                    if(mMarker.get(j).getTitle().equals(location)){
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarker.get(j).getPosition(),15));
                        markerBounce(mMarker.get(j));
                    }
                }
            }
        });

        mSearchText.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        ||keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        ||keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    String location = mSearchText.getText().toString();
                    for(int j=0; j<mMarker.size();j++){
                        if(mMarker.get(j).getTitle().equals(location)){
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarker.get(j).getPosition(),15));
                            markerBounce(mMarker.get(j));
                        }
                    }
                }
                return false;
            }
        });
    }

    private void addMarkersToMap() {
        mMarker = new ArrayList<>();
        for (int i=0; i<StopDetailsList.STOPS.length;i++){
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(StopDetailsList.STOPS[i].getPosition())
                    .title(StopDetailsList.STOPS[i].getName())
                    .snippet(StopDetailsList.STOPS[i].getDescription())
                    .icon(getIcon(score[i])));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)) //using custom icon
                    //.infoWindowAnchor(0.5f, 0.5f));
            mMarker.add(m);
        }
    }


    public BitmapDescriptor getIcon(int score){
        if (score<=30){
            //safe
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
        closeKeyboard();
    }

    //
    // Marker related listeners.
    //
    @Override
    public boolean onMarkerClick(final Marker marker) {
        markerBounce(marker);
        if (marker.equals(mSelectedMarker)) {
            mSelectedMarker = null;
            return true;
        }

        mSelectedMarker = marker;

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur.
        return false;

    }
    public void markerBounce(Marker marker){
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
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        String stopID = marker.getSnippet().split(" ")[1];

        Intent intent = new Intent(MapActivity.this, DetailActivity.class);
        intent.putExtra("stopID",stopID);
        startActivity(intent);
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        //Toast.makeText(this, "Close Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        //Toast.makeText(this, "Info Window long click", Toast.LENGTH_SHORT).show();
    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }



}
