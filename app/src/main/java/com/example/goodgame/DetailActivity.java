package com.example.goodgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity{

    private Button postBtn;
    private Button alertBtn;
    FirebaseAuth firebaseAuth;
    private String stopID;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        stopID = getIntent().getStringExtra("stopID");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //DetailFragment fragment= new DetailFragment();
        DetailFragment fragment= new DetailFragment(stopID);
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

        alertBtn = (Button)findViewById(R.id.action_alert);

        alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { sendAlert(); }
        });

/*
        if (!StopDetailsList.STOPS[Integer.parseInt(stopID)].getFreeZone()){
            alertBtn.setVisibility(View.VISIBLE);
            alertBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendAlert();
                }
            });
        }

 */




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
        intent.putExtra("stopID",stopID);
        startActivity(intent);

    }

    private void sendAlert(){
        String timeStamp = new SimpleDateFormat("yyyymmddHHmmss").format(new Date());
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Alerts/" + stopID);
        HashMap<Object,String> hashMap= new HashMap<>();
        hashMap.put("timestamp",timeStamp);
        ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DetailActivity.this,"Alert published",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failed adding post in database
                Toast.makeText(DetailActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent(DetailActivity.this, MapActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}