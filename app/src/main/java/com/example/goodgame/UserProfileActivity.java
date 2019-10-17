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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.goodgame.Photo.TakeProfilePhotoActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserProfileActivity extends AppCompatActivity {
    FirebaseUser user_;
    ImageView avatar_;
    TextView name_;
    TextView email_;
    String TAG = "Profile";
    Uri avatar_uri_;
    public static final int REQUSET = 1;
    Dialog mCameraDialog_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        user_ = FirebaseAuth.getInstance().getCurrentUser();
        email_ = (TextView) findViewById(R.id.email);
        name_ = (TextView) findViewById(R.id.name);
        avatar_ = (ImageView) findViewById(R.id.avatar);
        avatar_uri_ = null;
        setTitle("Profile");
        if(user_ != null){
            email_.setText(user_.getEmail());
            name_.setText(user_.getDisplayName());

            Uri u = user_.getPhotoUrl();
            if(u != null) {
                Log.e(TAG, u.toString());
                Glide.with(UserProfileActivity.this).load(u).error(R.drawable.default_avatar).into(avatar_);
            }
        }

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

        name_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeName(view);
            }
        });

    }
    public void switchAvatar(View view){
        Intent intent = new Intent(UserProfileActivity.this, TakeProfilePhotoActivity.class);
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

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = (int) getResources().getDisplayMetrics().widthPixels;
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        mCameraDialog_.show();
    }

    public class switchListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(UserProfileActivity.this, TakeProfilePhotoActivity.class);
            switch (view.getId()) {
                case R.id.btn_choose_img:

                    intent.putExtra("mode", 0);
                    startActivityForResult(intent, REQUSET);
                    break;
                case R.id.btn_open_camera:

                    intent.putExtra("mode", 1);
                    startActivityForResult(intent, REQUSET);
                    break;
                case R.id.btn_cancel:

                    break;


            }
            mCameraDialog_.dismiss();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UserProfileChangeRequest profileUpdates = null;
        switch (requestCode){
            case 1:
                String URI = data.getExtras().getString("URI");
                Log.i(TAG, URI);
                if(URI.equals("Failed") || URI.equals("Canceled") || URI == null){
                    return;
                }
                avatar_uri_ = Uri.parse(URI);

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference avatarRef = storage.getReference().child("avatars/"+avatar_uri_.getLastPathSegment());

                UploadTask uploadTask = avatarRef.putFile(avatar_uri_);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(UserProfileActivity.this, "Upload failed.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return avatarRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {

                                    Uri downloadUri = task.getResult();
                                    updateAvatar(downloadUri);

                                } else {
                                    Toast.makeText(UserProfileActivity.this, "Upload failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });
                break;
            case 2:
                String name = data.getExtras().getString("username");
                if(name == null || name.equals("")){
                    return;
                }

                profileUpdates = new UserProfileChangeRequest
                        .Builder()
                        .setDisplayName(name)
                        .build();

                user_.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Username updated.");
                                    Toast.makeText(UserProfileActivity.this, "Update complete.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                name_.setText(name);
                break;
        }

    }

    public void changeName(View v){
        Intent intent = new Intent(UserProfileActivity.this, ProfileNameChangeActivity.class);
        intent.putExtra("username", name_.getText());
        startActivityForResult(intent, 2);
    }

    public void updateAvatar(Uri u){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                .Builder()
                .setPhotoUri(u)
                .build();

        avatar_uri_ = u;
        Glide.with(UserProfileActivity.this).load(avatar_uri_).error(R.drawable.default_avatar).into(avatar_);

        user_.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User avatar updated.");
                            Toast.makeText(UserProfileActivity.this, "Update complete.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        BaseApplication.changeImage(avatar_uri_);
    }
}
