package com.example.goodgame.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodgame.PostDetailActivity;
import com.example.goodgame.R;
import com.example.goodgame.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;




public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyHolder>{

    Context context;
    List<ModelPost> postList;
    String myUid;

    private DatabaseReference likesRef;// for likes database node

    private DatabaseReference postsRef;// reference of posts

    boolean mProcessLike=false;


    public AdapterPost(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
        myUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        likesRef= FirebaseDatabase.getInstance().getReference().child("Likes");
        postsRef= FirebaseDatabase.getInstance().getReference().child("Posts");

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflate layout row_post.xml
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_posts,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        //get data
        String uid=postList.get(position).getUid();
        String uEmail=postList.get(position).getuEmail();
        String uName=postList.get(position).getuName();
        String uDp=postList.get(position).getuDp();
        String  pId=postList.get(position).getpId();
        String pTitle=postList.get(position).getpTitle();
        String pDescription=postList.get(position).getpDescr();
        String pImage=postList.get(position).getpImage();
        String pTimeStamp=postList.get(position).getpTime();
        String pLikes=postList.get(position).getpLikes();//cotains total number of likes for a post

        String pComments=postList.get(position).getpComments();

        //convert timestamp to dd/mm/yyy hh:mm am/pm
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        //set data
        holder.uNameTv.setText(uName);
        holder.pTimeTv.setText(pTime);
        holder.pTitleTv.setText(pTitle);
        holder.pDescriptionTV.setText(pDescription);
        holder.pLikesTv.setText(pLikes+" Likes");
        holder.pCommentsTv.setText(pComments+" Comments");
        //set likes for each post
        setLikes(holder,pId);

       //set user dp
//       try{
//           Picasso
//       }
//       catch (Exception e){
//
//       }

       //set post image
        if (pImage.equals("noImage")){
            holder.pImageIv.setVisibility(View.GONE);



        }
        else{
            holder.pImageIv.setVisibility(View.VISIBLE);
            try{
                Picasso.get().load(pImage).into(holder.pImageIv);
            }
            catch (Exception e){

            }
        }

        //handle button click


        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //will implement later
//                Toast.makeText(context,"Like",Toast.LENGTH_SHORT).show();
                final int pLikes=Integer.parseInt(postList.get(position).getpLikes());
                mProcessLike=true;
                //get id of post clicked
               final String postIde=postList.get(position).getpId();
                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(mProcessLike){
                            if(dataSnapshot.child(postIde).hasChild(myUid)){
                                //already liked before, so remove like
                                postsRef.child(postIde).child("pLikes").setValue(""+(pLikes-1));
                                likesRef.child(postIde).child(myUid).removeValue();
                                mProcessLike=false;

                            }
                            else {
                                //not liked, change it to like
                                postsRef.child(postIde).child("pLikes").setValue(""+(pLikes+1));
                                likesRef.child(postIde).child(myUid).setValue("Liked");// set any value
                                mProcessLike=false;

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //will implement later
//                Toast.makeText(context,"Comment",Toast.LENGTH_SHORT).show();

                //start PostDetailAct
                Intent intent=new Intent(context, PostDetailActivity.class);
                intent.putExtra("postId",pId);//will get detail of post using this id, its id of the post clicked
                context.startActivity(intent);


            }
        });
        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //will implement later
//                Toast.makeText(context,"Share",Toast.LENGTH_SHORT).show();
                /*handle only text and image and text*/
                //get image from imageview
                BitmapDrawable bitmapDrawable=(BitmapDrawable)holder.pImageIv.getDrawable();
                if(bitmapDrawable==null){
                    //post without image
                    shareTextOnly(pTitle,pDescription);

                }
                else {
                    //post with image
                    shareTextOnly(pTitle,pDescription);

                    //convert image into bitmap
//                    Bitmap bitmap=bitmapDrawable.getBitmap();
//                    shareImageAndText(pTitle,pDescription,bitmap);



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
        context.startActivity(Intent.createChooser(sIntent,"Share Via"));//message to show in share dialog






    }

    private void shareImageAndText(String pTitle, String pDescription, Bitmap bitmap) {

        //concatenate title and description to share
        String shareBody=pTitle+"\n"+pDescription;



        //1,save message in cache,get the saved image uri
        Uri uri=saveImageToShare(bitmap);

        //share intent
        Intent sIntent= new Intent(Intent.ACTION_SEND);
        sIntent.setAction(Intent.ACTION_VIEW);
        sIntent.putExtra(Intent.EXTRA_STREAM,uri);
        sIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sIntent.putExtra(Intent.EXTRA_TEXT,shareBody);//text to share
        sIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");//share via email

        sIntent.setType("image/png");
//        context.startActivity(Intent.createChooser(sIntent,"Share Via"));

//        sIntent.setType("image/png");
        context.startActivity(Intent.createChooser(sIntent,"Share Via"));





    }

    private Uri saveImageToShare(Bitmap bitmap) {
        File imageFolder=new File(context.getCacheDir(),"images");
        Uri uri=null;
        try{
            imageFolder.mkdirs();// create if not exists
            File file=new File(imageFolder,"shared_image.png");
            FileOutputStream stream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,90,stream);
            stream.flush();
            stream.close();
            uri= FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName()+ ".fileprovider"
                    ,file);



        }catch (Exception e){
            Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_SHORT).show();

        }


        return uri;
    }


    //add a key named "pLikes" to each post and set its value to "0" manually



    private void setLikes(MyHolder holder, String postKey) {
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(postKey).hasChild(myUid)){
                    //user has liked this post
                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked,0,0,0 );
                    holder.likeBtn.setText("Liked");

                }
                else {
                    //user has not liked this post
                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_black,0,0,0 );
                    holder.likeBtn.setText("Like");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        //views from row_posts.xml
          ImageView uPictureIv,pImageIv;
          TextView uNameTv,pTimeTv,pTitleTv,pDescriptionTV,pLikesTv,pCommentsTv;
          ImageButton moreBtn;
          Button likeBtn,commentBtn,shareBtn;




         public MyHolder(@NonNull View itemView) {
            super(itemView);
            // init views
             uPictureIv=itemView.findViewById(R.id.uPictureIv);
             pImageIv=itemView.findViewById(R.id. pImageIv );
             uNameTv=itemView.findViewById(R.id.uNameTv);
             pTimeTv=itemView.findViewById(R.id.pTimeTv);
             pTitleTv=itemView.findViewById(R.id.pTitleTv);
             pDescriptionTV=itemView.findViewById(R.id.pDescriptionTV);
             pLikesTv=itemView.findViewById(R.id.pLikesTv);
             pCommentsTv=itemView.findViewById(R.id.pCommentsTv);
             moreBtn=itemView.findViewById(R.id.moreBtn);
             likeBtn=itemView.findViewById(R.id.likeBtn);
             commentBtn=itemView.findViewById(R.id.commentBtn);
             shareBtn=itemView.findViewById(R.id.shareBtn);


        }
    }

}
