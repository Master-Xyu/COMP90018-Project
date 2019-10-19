package com.example.goodgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodgame.adapters.AdapterPost;
import com.example.goodgame.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private Button postBtn;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPost adapterPost;

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

        firebaseAuth=FirebaseAuth.getInstance();

        //recycler view and its properties
        recyclerView=findViewById(R.id.postsRecyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //show newesr post first,for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);


        //init post list
        postList= new ArrayList<>();
         loadPosts();



    }

    private void loadPosts() {
        //path of all posts

    }

    private void openPostActivity() {
        Intent intent =new Intent(this,AddPostActivity.class);
        startActivity(intent);

    }




//    /*inflate options menu*/
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.styled_map,menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    /*handle menu item clicks*/

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //get item id
//        int id =item.getItemId();
//        if(id == R.id.signout){
//            firebaseAuth.signOut();
//            checkUserStatus();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void checkUserStatus() {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){

        }
        else{
            //???
            startActivity(new Intent(DetailActivity.this,MapActivity.class));
            finish();
        }
    }
}