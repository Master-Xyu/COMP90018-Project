package com.example.goodgame.Photo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;
import java.util.ArrayList;


/**
 * - 支持通过相机拍照获取图片
 * - 支持从相册选择图片
 * - 支持从文件选择图片
 * - 支持多图选择
 * - 支持批量图片裁切
 * - 支持批量图片压缩
 * - 支持对图片进行压缩
 * - 支持对图片进行裁剪
 * - 支持对裁剪及压缩参数自定义
 * - 提供自带裁剪工具(可选)
 * - 支持智能选取及裁剪异常处理
 * - 支持因拍照Activity被回收后的自动恢复
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.cboy.me
 * GitHub:https://github.com/crazycodeboy
 * Eamil:crazycodeboy@gmail.com
 */
public class SimpleActivity extends TakePhotoActivity {
    private CustomHelper customHelper;
    private CropOptions.Builder cropBuilder_;
    private TakePhotoOptions.Builder takeBuilder_;
    private TakePhoto takePhoto_;
    private Uri imageUri_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermission();
        super.onCreate(savedInstanceState);
        createCropBuilder();
        takePhoto_ = getTakePhoto();

        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode", 2);

        File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        imageUri_ = Uri.fromFile(file);

        switch (mode){
            case 0:
                takePhoto_.onPickFromGalleryWithCrop(imageUri_,cropBuilder_.create());
                break;
            case 1:
                createTakeBuilder();
                Log.d("take photo","start!");
                Log.d("take photo",imageUri_.toString());
                takePhoto_.onPickFromCaptureWithCrop(imageUri_,cropBuilder_.create());
                Log.d("take photo","end!");
        }
        /*
        View contentView=LayoutInflater.from(this).inflate(R.layout.common_layout,null);
        setContentView(contentView);
        customHelper=CustomHelper.of(contentView);

         */
    }

    public void onClick(View view) {
        customHelper.onClick(view,getTakePhoto());
    }

    @Override
    public void takeCancel() {
        super.takeCancel();

        Intent intent = new Intent();
        intent.putExtra("URI", "Canceled");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);

        Intent intent = new Intent();
        intent.putExtra("URI", "Failed");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        //showImg(result.getImages());

        Intent intent = new Intent();
        intent.putExtra("URI", imageUri_.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showImg(ArrayList<TImage> images) {
        Intent intent=new Intent(this,ResultActivity.class);
        intent.putExtra("images",images);
        startActivity(intent);
    }

    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            Log.e("SimpleActivity", "checkPermission: 没授权！");

        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            Log.e("SimpleActivity", "checkPermission: 已经授权！");
        }
    }

    private void createCropBuilder(){
        cropBuilder_ =new CropOptions.Builder();
        cropBuilder_.setOutputX(96).setOutputY(96);
        cropBuilder_.setWithOwnCrop(true);
    }

    private void createTakeBuilder(){
        takeBuilder_ = new TakePhotoOptions.Builder();
        takeBuilder_.setCorrectImage(true);
        takePhoto_.setTakePhotoOptions(takeBuilder_.create());
    }
}
