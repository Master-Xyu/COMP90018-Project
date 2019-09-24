package com.example.goodgame;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        TextView email = (TextView) findViewById(R.id.email);
        TextView name = (TextView) findViewById(R.id.name);
        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        if(user != null){
            email.setText(user.getDisplayName());
            name.setText(user.getEmail());
            avatar.setImageURI(user.getPhotoUrl());
        }

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();
            transaction.replace(R.id.Comment_Frame, fragment);
            transaction.commit();
        }

    }
}
