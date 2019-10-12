package com.example.goodgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private Button postBtn;

    protected void onCreat(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        postBtn = (Button)findViewById(R.id.action_add_post);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPostActivity();
            }
        });
    }

    private void openPostActivity() {
        Intent intent =new Intent(this,AddPostActivity.class);
        startActivity(intent);

    }

}
