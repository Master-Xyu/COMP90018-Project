package com.example.goodgame;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.goodgame.Photo.SimpleActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Date;
import java.util.Locale;

public class UserProfileActivity extends AppCompatActivity {
    FirebaseUser user_;
    ImageView avatar_;
    EditText name_;
    TextView email_;
    String TAG = "Profile";
    Uri avatar_uri_;
    public static final int REQUSET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        user_ = FirebaseAuth.getInstance().getCurrentUser();
        email_ = (TextView) findViewById(R.id.email);
        name_ = (EditText) findViewById(R.id.name);
        avatar_ = (ImageView) findViewById(R.id.avatar);
        avatar_uri_ = createImagePathUri(this);

        if(user_ != null){
            email_.setText(user_.getEmail());
            name_.setText(user_.getDisplayName());
            //avatar_.setImageURI(user_.getPhotoUrl());
        }

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();
            transaction.replace(R.id.Comment_Frame, fragment);
            transaction.commit();
        }

    }
    public void switchAvatar(View view){
        Intent intent = new Intent(UserProfileActivity.this, SimpleActivity.class);
        startActivityForResult(intent, REQUSET);
        /*
        switch (view.getId()) {
            case R.id.avatar:
                //弹出对话框
                setDialog();
                break;
            case R.id.btn_choose_img:
                //选择照片按钮
                Toast.makeText(this, "请选择照片", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_open_camera:
                //拍照按钮
                Toast.makeText(this, "即将打开相机", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cancel:
                //取消按钮
                Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                break;
        }

         */
    }

    private void setDialog() {
        Dialog mCameraDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.pop_dialog, null);

        switchListener sl = this.new switchListener();
        root.findViewById(R.id.btn_choose_img).setOnClickListener(sl);
        root.findViewById(R.id.btn_open_camera).setOnClickListener(sl);
        root.findViewById(R.id.btn_cancel).setOnClickListener(sl);

        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
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
        mCameraDialog.show();
    }

    public void updateOnClick(View v){

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name_.getText().toString())
                .setPhotoUri(avatar_uri_)
                .build();

        user_.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    public class switchListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            /*
            switch (view.getId()) {
                case R.id.btn_choose_img:
                    //选择照片按钮
                    Toast.makeText(UserProfileActivity.this, "请选择照片", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_open_camera:
                    //拍照按钮
                    Toast.makeText(UserProfileActivity.this, "即将打开相机", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_cancel:
                    //取消按钮
                    Toast.makeText(UserProfileActivity.this, "取消", Toast.LENGTH_SHORT).show();
                    break;
            }

             */
        }
    }

    private static Uri createImagePathUri(Context context) {
        Uri imageFilePath = null;
        String status = Environment.getExternalStorageState();
        SimpleDateFormat timeFormatter = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.ENGLISH);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
            imageFilePath = context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            imageFilePath = context.getContentResolver().insert(
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
        }
        Log.i("", "生成的照片输出路径：" + imageFilePath.toString());
        return imageFilePath;
    }

}
