package com.example.goodgame;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class ListActivity extends AppCompatActivity {


    FirebaseFirestore db;
    private TextView StopDisplay;
    private TextView GPSDisplay;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private static LatLng myLocation;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
/*
        db.collection("stopInfo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            StringBuilder fields = new StringBuilder("");
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                fields.append("Name: ").append(d.getString("name"));
                                fields.append("\nDescription: ").append(d.getString("description")).append("\n");
                                fields.append("\n");
                            }
                            textDisplay.setText(fields.toString());
                        }
                    }
                });
 */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ListActivity.this, MapActivity.class);
                startActivity(intent2);
            }
        });

        Button btnList = (Button) findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(ListActivity.this, ListActivity.class);
                startActivity(intent3);
            }
        });

        StopDisplay = findViewById(R.id.textView);
        StopDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(ListActivity.this, DetailActivity.class);
                startActivity(intent4);
            }
        });

        GPSDisplay = (TextView) findViewById(R.id.textView7);
        //using gps
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            //Whenever the location is updated, the last method checks if the gps is turned off.
            @Override
            public void onLocationChanged(Location location) {
                //GPSDisplay.append("\n" + location.getLatitude() + location.getLongitude());
                myLocation = new LatLng(location.getLatitude(),location.getLongitude());
                StringBuilder fields = new StringBuilder("");
                int[] id = sortLocation(myLocation);
                for (int i = 0; i<id.length; i++){
                    fields.append("Name: ").append(StopDetailsList.STOPS[i].getName());
                    fields.append("\nDescription: ").append(StopDetailsList.STOPS[i].getDescription()).append("\n");
                    fields.append("\n");
                }
                StopDisplay.setText(fields.toString());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent4 = new Intent(ListActivity.this, DetailActivity.class);
                startActivity(intent4);
            }


        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            }
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
    }


    public int[] sortLocation(LatLng currentLocation){
        double[] dis = new double[StopDetailsList.STOPS.length];
        int[] index = new int[dis.length];
        HashMap map = new HashMap();
        for (int i = 0; i < dis.length; i++){
            dis[i] = SphericalUtil.computeDistanceBetween(currentLocation, StopDetailsList.STOPS[i].getPosition());
            map.put(dis[i], i);
        }
        Arrays.sort(dis); // 升序排列

        // 查找原始下标
        for (int i = 0; i < dis.length; i++) {
            index[i] = (int) map.get(dis[i]);
        }
        return index;
    }




}
