package com.example.goodgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {

    private static final String name = "Name";
    private static final String description = "Description";
    private static final Boolean isFreeZone = false;
    FirebaseFirestore db;
    TextView textDisplay;
    String TAG = "ListActivity";
    ArrayList<JSONObject> stopArray= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

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

        ReadSingleContact();



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
