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
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;
import java.util.ArrayList;

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
        configCompress(takePhoto_);
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Please authorize to use this app.", Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        } else {
            //Toast.makeText(this, "authorization succeeds！", Toast.LENGTH_SHORT).show();

        }
    }

    private void createCropBuilder(){
        cropBuilder_ =new CropOptions.Builder();
        cropBuilder_.setWithOwnCrop(true);
        cropBuilder_.setAspectX(100).setAspectY(100);
    }

    private void createTakeBuilder(){
        takeBuilder_ = new TakePhotoOptions.Builder();
        takeBuilder_.setCorrectImage(true);
        takePhoto_.setTakePhotoOptions(takeBuilder_.create());
    }

    private void configCompress(TakePhoto takePhoto){

        CompressConfig config;
        config=new CompressConfig.Builder()
                .setMaxSize(102400)
                .setMaxPixel(100)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config,true);

    }
}
