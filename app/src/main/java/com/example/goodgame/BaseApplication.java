package com.example.goodgame;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.example.goodgame.Float.FloatWindow;
import com.example.goodgame.Float.MoveType;
import com.example.goodgame.Float.PermissionListener;
import com.example.goodgame.Float.Screen;
import com.example.goodgame.Float.ViewStateListener;

/**
 * Created by yhao on 2017/12/18.
 * https://github.com/yhaolpz
 */

public class BaseApplication extends Application {


    private static final String TAG = "FloatWindow";
    public static ImageView imageView_;
    @Override
    public void onCreate() {
        super.onCreate();

        imageView_ = new ImageView(getApplicationContext());
        imageView_.setImageResource(R.drawable.default_avatar);

        FloatWindow
                .with(getApplicationContext())
                .setView(imageView_)
                .setWidth(Screen.width, 0.15f) //设置悬浮控件宽高
                .setHeight(Screen.width, 0.15f)
                .setX(Screen.width, 0.8f)
                .setY(Screen.height, 0.3f)
                .setMoveType(MoveType.slide,100,100)
                .setMoveStyle(500, new BounceInterpolator())
                .setFilter(true, MapActivity.class, ListActivity.class)
                .setViewStateListener(mViewStateListener)
                .setPermissionListener(mPermissionListener)
                .setDesktopShow(false)
                .build();


        imageView_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BaseApplication.this.getApplicationContext(), UserProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "onSuccess");
        }

        @Override
        public void onFail() {
            Log.d(TAG, "onFail");
        }
    };

    private ViewStateListener mViewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int x, int y) {
            Log.d(TAG, "onPositionUpdate: x=" + x + " y=" + y);
        }

        @Override
        public void onShow() {
            Log.d(TAG, "onShow");
        }

        @Override
        public void onHide() {
            Log.d(TAG, "onHide");
        }

        @Override
        public void onDismiss() {
            Log.d(TAG, "onDismiss");
        }

        @Override
        public void onMoveAnimStart() {
            Log.d(TAG, "onMoveAnimStart");
        }

        @Override
        public void onMoveAnimEnd() {
            Log.d(TAG, "onMoveAnimEnd");
        }

        @Override
        public void onBackToDesktop() {
            Log.d(TAG, "onBackToDesktop");
        }
    };

    public static void changeImage(Uri uri){
        imageView_.setImageURI(uri);
    }
}
