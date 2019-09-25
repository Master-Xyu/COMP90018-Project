package com.example.goodgame;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {
    FirebaseUser user;
    ImageView avatar;
    EditText name;
    TextView email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = (TextView) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        avatar = (ImageView) findViewById(R.id.avatar);

        if(user != null){
            email.setText(user.getDisplayName());
            name.setText(user.getEmail());
            avatar.setImageURI(user.getPhotoUrl());
        }

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();
            transaction.replace(R.id.Comment_Frame, fragment);
            transaction.commit();
        }

    }
    public void switchAvatar(View view){
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
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

    public class switchListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
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
        }
    }
}
