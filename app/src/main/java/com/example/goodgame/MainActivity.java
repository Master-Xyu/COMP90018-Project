package com.example.goodgame;

<<<<<<< Updated upstream
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
=======
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
>>>>>>> Stashed changes

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button btnMap = (Button) findViewById(R.id.btn);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
}
