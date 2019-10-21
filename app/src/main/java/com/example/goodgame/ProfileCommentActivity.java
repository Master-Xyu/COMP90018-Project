package com.example.goodgame;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileCommentActivity extends AppCompatActivity {

    protected ArrayList<String> mDataset;
    protected ArrayList<String> mDataID;
    protected ArrayList<Integer> mDataType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_comment);
        initDataset();
    }

    private void initDataset() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Posts");
        String UID = FirebaseAuth.getInstance().getUid();
        Query query = mDatabase.equalTo(UID);
        mDataset = new ArrayList();
        mDataType = new ArrayList();
        mDataID = new ArrayList();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String pId = postSnapshot.child("pId").getValue().toString();
                    if (postSnapshot.child("uid").getValue().toString().equals(UID)) {

                        mDataset.add(postSnapshot.child("pDescr").getValue().toString());
                        mDataType.add(1);
                        mDataID.add(pId);
                    }
                    for (DataSnapshot commentSnapshot : postSnapshot.child("Comments").getChildren()) {
                        if (commentSnapshot.child("uid").getValue().toString().equals(UID)) {
                            mDataset.add(commentSnapshot.child("comment").getValue().toString());
                            mDataType.add(2);
                            mDataID.add(pId + " " + commentSnapshot.child("cId").getValue().toString());
                        }
                    }
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                RecyclerViewFragment fragment = new RecyclerViewFragment();
                fragment.mDataID = mDataID;
                fragment.mDataset = mDataset;
                fragment.mDataType = mDataType;
                transaction.replace(R.id.Comment_Frame, fragment);
                transaction.commit();
                setTitle("Comments");
                mDatabase.removeEventListener(this);
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Comment", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }
}
