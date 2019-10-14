package com.example.goodgame;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goodgame.Photo.SimpleActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {
    FirebaseUser user_;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    //path to ???

    ImageView avatar_;
    EditText name_;
    TextView email_;
    String TAG = "Profile";
    Uri avatar_uri_;
    public static final int REQUSET = 1;
    Dialog mCameraDialog_;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        storageReference= FirebaseStorage.getInstance().getReference();


        user_ = FirebaseAuth.getInstance().getCurrentUser();
        email_ = (TextView) findViewById(R.id.email);
        name_ = (EditText) findViewById(R.id.name);
        avatar_ = (ImageView) findViewById(R.id.avatar);
        avatar_uri_ = null;


        //??? get info of current signed in user
        Query query=databaseReference.orderByChild("email").equalTo(user_.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check until required data get
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    //get data
                    String name=""+ds.child("name").getValue();
                    String email=""+ds.child("email").getValue();
                    String image=""+ds.child("image").getValue();
                    email_.setText(email);
                    name_.setText(name);
                    //try to add image
                    try{
                        //if image is received then set
                        Picasso.get().load(image).into(avatar_);
                    }
                    catch (Exception e){
                        //if there is ang exception while getting image then set default
                        Picasso.get().load(R.drawable.default_avatar).into(avatar_);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        setTitle("Profile");
        //replaced by firebase way
//        if(user_ != null){
////            email_.setText(user_.getEmail());
////            name_.setText(user_.getDisplayName());
//            Uri u = user_.getPhotoUrl();
//            if(u != null)
//                avatar_.setImageURI(u);
//        }

        Button bt = (Button) findViewById(R.id.signout);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserProfileActivity.this, EmailPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
    //change profile??
    public void switchAvatar(View view){
        Intent intent = new Intent(UserProfileActivity.this, SimpleActivity.class);
        setDialog();
    }

    public void showComments(View view){
        Intent intent = new Intent(UserProfileActivity.this,ProfileCommentActivity.class);
        startActivity(intent);
    }

    private void setDialog() {
        mCameraDialog_ = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.pop_dialog, null);

        switchListener sl = this.new switchListener();
        root.findViewById(R.id.btn_choose_img).setOnClickListener(sl);
        root.findViewById(R.id.btn_open_camera).setOnClickListener(sl);
        root.findViewById(R.id.btn_cancel).setOnClickListener(sl);

        mCameraDialog_.setContentView(root);
        Window dialogWindow = mCameraDialog_.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
 //       dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog_.show();
    }

    public void updateOnClick(View v){
        //path and name stored in firebase
//        String filePath=storageReference+""+avatar_uri_+""+user_.getUid();



        UserProfileChangeRequest.Builder profileUpdatesBuilder = new UserProfileChangeRequest.Builder();
        profileUpdatesBuilder.setDisplayName(name_.getText().toString());
        if(avatar_uri_ != null){
            profileUpdatesBuilder.setPhotoUri(avatar_uri_);
        }
        UserProfileChangeRequest profileUpdates = profileUpdatesBuilder.build();

        user_.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });

        //path and name stored in firebase
        String filePathAndName=storageReference+""+avatar_uri_+""+user_.getUid();
        StorageReference storageReference2=storageReference.child(filePathAndName);
        storageReference2.putFile(avatar_uri_).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri=uriTask.getResult();
                        if(uriTask.isSuccessful()){
                            HashMap<String,Object> results=new HashMap<>();
                            results.put(filePathAndName,downloadUri.toString());
                            databaseReference.child(user_.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public class switchListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(UserProfileActivity.this, SimpleActivity.class);
            switch (view.getId()) {
                case R.id.btn_choose_img:
                    //选择照片按钮
                    Toast.makeText(UserProfileActivity.this, "请选择照片", Toast.LENGTH_SHORT).show();
                    intent.putExtra("mode", 0);
                    startActivityForResult(intent, REQUSET);
                    break;
                case R.id.btn_open_camera:
                    //拍照按钮
                    Toast.makeText(UserProfileActivity.this, "即将打开相机", Toast.LENGTH_SHORT).show();
                    intent.putExtra("mode", 1);
                    startActivityForResult(intent, REQUSET);
                    break;
                case R.id.btn_cancel:
                    //取消按钮
                    Toast.makeText(UserProfileActivity.this, "取消", Toast.LENGTH_SHORT).show();
                    break;


            }
            mCameraDialog_.dismiss();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String URI = data.getExtras().getString("URI");
        Log.i(TAG, URI);
        if(URI == "Failed" || URI == "Canceled"){
            return;
        }
        avatar_uri_ = Uri.parse(URI);
        avatar_.setImageURI(avatar_uri_);
    }

}
