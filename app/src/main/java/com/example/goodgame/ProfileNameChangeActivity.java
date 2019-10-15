package com.example.goodgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileNameChangeActivity extends AppCompatActivity {

    private EditText newName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_name_change);

        newName = (EditText) findViewById(R.id.newName);
        Intent intent = getIntent();
        newName.setText(intent.getStringExtra("username"));
    }

    public void confirm(View v){
        Intent intent = new Intent();
        intent.putExtra("username", newName.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        cancel(this.getWindow().getDecorView());
    }

    public void cancel(View v){
        Intent intent = new Intent();
        intent.putExtra("username", "");
        setResult(RESULT_OK, intent);
        finish();
    }

}
