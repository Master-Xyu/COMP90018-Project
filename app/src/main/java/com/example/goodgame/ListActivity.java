package com.example.goodgame;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class ListActivity extends AppCompatActivity {


    FirebaseFirestore db;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private static LatLng myLocation;
    private ArrayList<StopDetails> mStop = new ArrayList<>();
    RecyclerViewAdapter adapter;

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
        setTitle("");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getPhotoUrl() != null){
            BaseApplication.changeImage(user.getPhotoUrl());
        }
        
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



        //using gps
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            //Whenever the location is updated, the last method checks if the gps is turned off.
            @Override
            public void onLocationChanged(Location location) {
                myLocation = new LatLng(location.getLatitude(),location.getLongitude());

                mStop.clear();
                double[] dis = new double[StopDetailsList.STOPS.length];
                int[] index = new int[dis.length];
                HashMap map = new HashMap();
                for (int i = 0; i < dis.length; i++){
                    dis[i] = SphericalUtil.computeDistanceBetween(myLocation, StopDetailsList.STOPS[i].getPosition());
                    map.put(dis[i], i);
                }
                Arrays.sort(dis); // 升序排列
                // 查找原始下标
                for (int i = 0; i < dis.length; i++) {
                    index[i] = (int) map.get(dis[i]);
                }

                for (int i = 0; i<index.length; i++){
                    mStop.add(StopDetailsList.STOPS[index[i]]);
                    if (Math.round(dis[i])<=10){
                        StopDetailsList.STOPS[index[i]].setDistance("<=10m");
                    }
                    else{
                        StopDetailsList.STOPS[index[i]].setDistance(Math.round(dis[i])+"m");
                    }
                }
                initRecyclerView();
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
        locationManager.requestLocationUpdates("gps", 10000, 0, locationListener);
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        adapter = new RecyclerViewAdapter(mStop, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


}
