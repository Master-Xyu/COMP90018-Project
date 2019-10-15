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

import com.example.goodgame.Photo.SimpleActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

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
                Picasso.get().load(u).into(avatar_);
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
        UserProfileChangeRequest profileUpdates = null;
        switch (requestCode){
            case 1:
                String URI = data.getExtras().getString("URI");
                Log.i(TAG, URI);
                if(URI.equals("Failed") || URI.equals("Canceled") || URI == null){
                    return;
                }

                profileUpdates = new UserProfileChangeRequest
                        .Builder()
                        .setPhotoUri(avatar_uri_)
                        .build();

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

                avatar_uri_ = Uri.parse(URI);
                avatar_.setImageURI(avatar_uri_);
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
}
