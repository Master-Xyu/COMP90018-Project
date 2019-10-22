package com.example.goodgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodgame.adapters.AdapterPost;
import com.example.goodgame.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.FragmentTransaction;

public class DetailActivity extends AppCompatActivity{

    private Button postBtn;
    FirebaseAuth firebaseAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DetailFragment fragment= new DetailFragment();
//        Intent intent = new Intent(DetailActivity.this,DetailFragment.class);
//        startActivity(intent);
        transaction.replace(R.id.detail_fragment, fragment);
        //transaction.addToBackStack(fragment.toString());
        //transaction.setTransition(transaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
        setTitle("Detail");
        postBtn = (Button)findViewById(R.id.action_add_post);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPostActivity();
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
//
//        //recycler view and its properties
//        recyclerView=findViewById(R.id.postsRecyclerview);
//        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
//        //show newesr post first,for this load from last
//        layoutManager.setStackFromEnd(true);
//        layoutManager.setReverseLayout(true);
//        //set layout to recyclerview
//        recyclerView.setLayoutManager(layoutManager);
//
//
//        //init post list
//        postList= new ArrayList<>();
//         loadPosts();



    }

//    private void loadPosts() {
//        //path of all posts
//        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
//        //get all data from this ref
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                postList.clear();
//                for (DataSnapshot ds:dataSnapshot.getChildren()){
//                    ModelPost modelPost=ds.getValue(ModelPost.class);
//                    postList.add(modelPost);
//
//                    //adapter
//                    adapterPost=new AdapterPost(DetailActivity.this,postList);
//                    //set adapter to recyclerview
//                    recyclerView.setAdapter(adapterPost);
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //in case of error
//                Toast.makeText(DetailActivity.this,""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }
//    private void searchPosts(String searchQuery){
//        //path of all posts
//        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
//        //get all data from this ref
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                postList.clear();
//                for (DataSnapshot ds:dataSnapshot.getChildren()){
//                    ModelPost modelPost=ds.getValue(ModelPost.class);
//                    if(modelPost.getpTitle().toLowerCase().contains(searchQuery.toLowerCase())||
//                            modelPost.getpDescr().toLowerCase().contains(searchQuery.toLowerCase()) ) {
//                        postList.add(modelPost);
//                    }
//
//                    //adapter
//                    adapterPost=new AdapterPost(DetailActivity.this,postList);
//                    //set adapter to recyclerview
//                    recyclerView.setAdapter(adapterPost);
//
//
//                }
//
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //in case of error
//                Toast.makeText(DetailActivity.this,""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        return super.onCreateView(name, context, attrs);
//        firebaseAuth=FirebaseAuth.getInstance();
//
//        //recycler view and its properties
//        recyclerView=findViewById(R.id.postsRecyclerview);
//        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
//        //show newesr post first,for this load from last
//        layoutManager.setStackFromEnd(true);
//        layoutManager.setReverseLayout(true);
//        //set layout to recyclerview
//        recyclerView.setLayoutManager(layoutManager);
//
//
//        //init post list
//        postList= new ArrayList<>();
//        loadPosts();
//    }

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