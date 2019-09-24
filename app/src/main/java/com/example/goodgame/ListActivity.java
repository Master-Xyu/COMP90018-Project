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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ListActivity extends AppCompatActivity {

    private static final String name = "Name";
    private static final String description = "Description";
    private static final Boolean isFreeZone = false;
    FirebaseFirestore db;
    TextView textDisplay;
    String TAG = "ListActivity";
    ArrayList<JSONObject> stopArray= new ArrayList<>();
    private TextView textView;
    private LocationListener locationListener;
    private LocationManager locationManager;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        stopArray = new ArrayList<>();
        //ReadSingleContact();

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


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ListActivity.this, UserProfileActivity.class);
                startActivity(intent1);

            }
        });
        */

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

        textDisplay = findViewById(R.id.textView);
        textDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(ListActivity.this, DetailActivity.class);
                startActivity(intent4);
            }
        });

        textView = (TextView) findViewById(R.id.textView7);
        //using gps
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            //Whenever the location is updated, the last method checks if the gps is turned off.
            @Override
            public void onLocationChanged(Location location) {
                textView.append("\n" + location.getLatitude() + location.getLongitude());
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

    private void ReadSingleContact() {
        CollectionReference collectionReference = db.collection("stopInfo");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    StringBuilder fields = new StringBuilder("");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        JSONObject stopInfo = new JSONObject();
                        JSONObject stop = new JSONObject();

                        fields.append("Name: ").append(document.get("name"));
                        fields.append("\nDescription: ").append(document.get("description")).append("\n");
                        fields.append("\n");

                        try {
                            stop.put("name", document.get("name"));
                            stop.put("description",document.get("description"));
                            stopInfo.put("id",document.getId());
                            stopInfo.put("info", stop);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    textDisplay.setText(fields.toString());
                }
            }
        });

    }


}
