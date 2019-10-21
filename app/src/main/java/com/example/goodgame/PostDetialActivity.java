package com.example.goodgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodgame.adapters.AdapterComments;
import com.example.goodgame.models.ModelComment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PostDetialActivity extends AppCompatActivity {

    //to get detail of user and post
    String myUid,myEmail,myName,myDp,
    postId,pLikes,hisDp,hisName;

    boolean myProcessComment=false;
    boolean mProcessLike=false;
    ProgressDialog pd;


    //views
    ImageView uPictureIv,pImageIv;
    TextView uNameTv,pTimeTiv,pTitleTv,pDescriptionTv,pLikesTv,pCommentsTv;
    ImageButton moreBtn;
    Button likeBtn,shareBtn;
    RecyclerView recyclerView;


    List<ModelComment> commentList;
    AdapterComments adapterComments;


    //add comments views
    EditText commentEt;
    ImageButton sendBtn;
    ImageView cAvatarIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detial);

        //Actionbar and its properties
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Post Detail");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get id of post using intent
        Intent intent=getIntent();
        postId=intent.getStringExtra("postId");
        




        //init
        uPictureIv=findViewById(R.id.uPictureIv);
        pImageIv=findViewById(R.id.pImageIv);
        uNameTv=findViewById(R.id.uNameTv);
        pTimeTiv=findViewById(R.id.pTimeTv);
        pTitleTv=findViewById(R.id.pTitleTv);
        pDescriptionTv=findViewById(R.id.pDescriptionTV);
        pLikesTv=findViewById(R.id.pLikesTv);
        pCommentsTv=findViewById(R.id.pCommentsTv);
        moreBtn=findViewById(R.id.moreBtn);
        likeBtn=findViewById(R.id.likeBtn);
        shareBtn=findViewById(R.id.shareBtn);
        recyclerView=findViewById(R.id.recyclerView);

        commentEt=findViewById(R.id.commentEt);
        sendBtn=findViewById(R.id.sendBtn);
        cAvatarIv=findViewById(R.id.cAvatarIv);


        loadPostInfo();

        checkUserStatus();

        loadUserInfo();

        setLike();


        //set subtitle of actionbar
        actionBar.setSubtitle("SignedIn as:"+ myEmail);
        
        loadComments();
        
        
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });


        //like button click handle
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likePost();
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pTitle=pTitleTv.getText().toString().trim();
                String pDescription=pDescriptionTv.getText().toString().trim();

                BitmapDrawable bitmapDrawable=(BitmapDrawable)pImageIv.getDrawable();
                if(bitmapDrawable==null){
                    //post without image
                    shareTextOnly(pTitle,pDescription);

                }
                else {
                    //post with image

                    //convert image into bitmap
                    Bitmap bitmap=bitmapDrawable.getBitmap();
                    shareImageAndText(pTitle,pDescription,bitmap);



                }


            }
        });


    }


    private void shareTextOnly(String pTitle, String pDescription) {
        //concatenate title and description to share
        String shareBody=pTitle+"\n"+pDescription;

        //share Intent
        Intent sIntent= new Intent(Intent.ACTION_SEND);
        sIntent.setType("text/plain");
        sIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");//share via email
        sIntent.putExtra(Intent.EXTRA_TEXT,shareBody);//text to share
        startActivity(Intent.createChooser(sIntent,"Share Via"));//message to show in share dialog






    }

    private void shareImageAndText(String pTitle, String pDescription, Bitmap bitmap) {

        //concatenate title and description to share
        String shareBody=pTitle+"\n"+pDescription;



        //1,save message in cache,get the saved image uri
        Uri uri=saveImageToShare(bitmap);

        //share intent
        Intent sIntent= new Intent(Intent.ACTION_SEND);
        sIntent.putExtra(Intent.EXTRA_STREAM,uri);
        sIntent.putExtra(Intent.EXTRA_TEXT,shareBody);//text to share
        sIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");//share via email

        sIntent.setType("image/png");
      startActivity(Intent.createChooser(sIntent,"Share Via"));





    }

    private Uri saveImageToShare(Bitmap bitmap) {
        File imageFolder=new File(getCacheDir(),"images");
        Uri uri=null;
        try{
            imageFolder.mkdirs();// create if not exists
            File file=new File(imageFolder,"shared_image.png");
            FileOutputStream stream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,90,stream);
            stream.flush();
            stream.close();
            uri= FileProvider.getUriForFile(this,"com.example.goodgame.fileprovider"
                    ,file);





        }catch (Exception e){
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

        }


        return uri;
    }





    private void loadComments() {
        //layout(linear) for recyclerview
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        // set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);

        //init comment list
        commentList=new ArrayList<>();

        //path of the post, to get the comments
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                commentList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelComment modelComment=ds.getValue(ModelComment.class);
                    commentList.add(modelComment);

                    //setup adapter
                    adapterComments=new AdapterComments(getApplicationContext(),commentList);
                    //set adapter
                    recyclerView.setAdapter(adapterComments);





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







    }

    private void setLike() {
        final DatabaseReference likesRef=FirebaseDatabase.getInstance().getReference().child("Likes");

        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(postId).hasChild(myUid)){
                    //user has liked this post
                    likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked,0,0,0 );
                    likeBtn.setText("Liked");

                }
                else {
                    //user has not liked this post
                   likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_black,0,0,0 );
                   likeBtn.setText("Like");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void likePost() {
        //will implement later
//                Toast.makeText(context,"Like",Toast.LENGTH_SHORT).show();
//        final int pLikes=Integer.parseInt(postList.get(position).getpLikes());
        mProcessLike=true;
        //get id of post clicked
//        final String postIde=postList.get(position).getpId();
        final DatabaseReference likesRef=FirebaseDatabase.getInstance().getReference().child("Likes");
        final DatabaseReference postsRef=FirebaseDatabase.getInstance().getReference().child("Posts");
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mProcessLike){
                    if(dataSnapshot.child(postId).hasChild(myUid)){
                        //already liked before, so remove like
                        postsRef.child(postId).child("pLikes").setValue(""+(Integer.parseInt(pLikes)-1));
                        likesRef.child(postId).child(myUid).removeValue();
                        mProcessLike=false;

//                        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_black,0,0,0);
//
//                        likeBtn.setText("Like");

                    }
                    else {
                        //not liked, change it to like
                        postsRef.child(postId).child("pLikes").setValue(""+(Integer.parseInt(pLikes)+1));
                        likesRef.child(postId).child(myUid).setValue("Liked");// set any value
                        mProcessLike=false;
//                        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked,0,0,0);
//
//                        likeBtn.setText("Liked");


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void postComment() {
        pd=new ProgressDialog(this);
        pd.setMessage("Adding comment...");


        //get data from comment edit text
        String comment=commentEt.getText().toString().trim();

        if (TextUtils.isEmpty(comment)){
            Toast.makeText(this,"Comment is empty....",Toast.LENGTH_SHORT).show();
            return;
        }

        String timeStamp =String.valueOf(System.currentTimeMillis());

        //each post will have a child "Comments" that will contain comments of that post
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("cId",timeStamp);
        hashMap.put("comment",comment);
        hashMap.put("timestamp",timeStamp);
        hashMap.put("uid",myUid);
        hashMap.put("uEmail",myEmail);
        hashMap.put("uDp",myDp);
        hashMap.put("uName",myName);

        //put this data in db
        ref.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();

                        Toast.makeText(PostDetialActivity.this,"Comment added...",Toast.LENGTH_SHORT).show();
                         commentEt.setText("");
                        updataCommentCount();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       pd.dismiss();
                        Toast.makeText(PostDetialActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();


                    }
                });


    }



    private void updataCommentCount() {
        //increase comment count
        myProcessComment=true;
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(myProcessComment){
                    String comments=""+dataSnapshot.child("pComments").getValue();
                    int newCommentBal=Integer.parseInt(comments)+1;
                    ref.child("pComments").setValue(""+newCommentBal);
                    myProcessComment=false;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadUserInfo() {
        //GET CURRENT USER INFO
        Query myRef=FirebaseDatabase.getInstance().getReference("Users");
        myRef.orderByChild("uid").equalTo(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot ds: dataSnapshot.getChildren()){
                     myName=""+ds.child("name").getValue();
                     myDp=""+ds.child("image").getValue();

                     try{
                         Picasso.get().load(myDp).placeholder(R.drawable.default_avatar).into(cAvatarIv);

                     }catch (Exception e){
                         Picasso.get().load(R.drawable.default_avatar).into(cAvatarIv);


                     }

                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void loadPostInfo() {
        //get post using the id of the post
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
        Query query =ref.orderByChild("pId").equalTo(postId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //keep checking the posts until get the required post
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    //get data
                    String pTitle=""+ds.child("pTitle").getValue();
                    String pDescr=""+ds.child("pDescr").getValue();
                    pLikes=""+ds.child("pLikes").getValue();
                    String pTimeStamp=""+ds.child("pTime").getValue()
                            ;
                    String pImage=""+ds.child("pImage").getValue();
                    hisDp=""+ds.child("uDp").getValue();
                    String uid=""+ds.child("uid").getValue();
                    String uEmail=""+ds.child("uEmail").getValue();
                    hisName=""+ds.child("uName").getValue();
                    String commentCount=""+ds.child("pComments").getValue();

                    //convert timestamp to dd/mm/yyy hh:mm am/pm
                    Calendar calendar=Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
                    String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

                    //set data
                    pTitleTv.setText(pTitle);
                    pDescriptionTv.setText(pDescr);
                    pLikesTv.setText(pLikes+"Likes");
                    pTimeTiv.setText(pTime);
                    pCommentsTv.setText(commentCount+" Comments");

                    uNameTv.setText(hisName);

                    //set post image
                    if (pImage.equals("noImage")){
                        pImageIv.setVisibility(View.GONE);



                    }
                    else{
                        pImageIv.setVisibility(View.VISIBLE);
                        try{
                            Picasso.get().load(pImage).into(pImageIv);
                        }
                        catch (Exception e){

                        }
                    }

                    //set user image in comment part
                    try{
                        Picasso.get().load(hisDp).placeholder(R.drawable.default_avatar).into(uPictureIv);

                    }catch (Exception e){
                        Picasso.get().load(R.drawable.default_avatar).into(uPictureIv);


                    }


                }
            }





            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void checkUserStatus(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            myEmail=user.getEmail();
            myUid=user.getUid();

        }
        else {
            Intent intent=new Intent(PostDetialActivity.this,BaseActivity.class);
            startActivity(intent);
            finish();

        }

    }


}
