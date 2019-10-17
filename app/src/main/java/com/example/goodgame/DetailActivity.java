package com.example.goodgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DetailActivity extends AppCompatActivity {

    private Button postBtn;
    FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState) {
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
        //Intent intent =new Intent(this,AddPostActivity.class);
        //startActivity(intent);

    }

    /*inflate options menu*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.styled_map,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*handle menu item clicks*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get item id
        int id =item.getItemId();
        if(id == R.id.signout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkUserStatus() {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){

        }
        else{
            //???
            startActivity(new Intent(DetailActivity.this,EmailPasswordActivity.class));
            finish();
        }
    }
}