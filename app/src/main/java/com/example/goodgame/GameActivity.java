package com.example.goodgame;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView tram;
    private ImageView person_1;
    private ImageView person_2;
    private ImageView person_3;
    private ImageView person_4;
    private ImageView bad_guy;

    //Size
    private int frameHeight;
    private int tramHeight;
    private int screenWidth;
    private int screenHeight;

    //Position
    private int tramY;
    private int person_1X;
    private int person_1Y;
    private int person_2X;
    private int person_2Y;
    private int person_3X;
    private int person_3Y;
    private int person_4X;
    private int person_4Y;
    private int bad_guyX;
    private int bad_guyY;

    private int score = 0;




    //Initialize Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //Status Check
    private boolean action_flag = false;
    private boolean start_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        scoreLabel = (TextView)findViewById(R.id.scoreLabel);
        startLabel = (TextView)findViewById(R.id.startLabel);
        tram = (ImageView)findViewById(R.id.tram);
        person_1 = (ImageView)findViewById(R.id.person_1);
        person_2 = (ImageView)findViewById(R.id.person_2);
        person_3 = (ImageView)findViewById(R.id.person_3);
        person_4 = (ImageView)findViewById(R.id.person_4);
        bad_guy = (ImageView)findViewById(R.id.bad_guy);

        //Get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;



        //Move to out of screen
        person_1.setX(-180);
        person_1.setY(-180);
        person_2.setX(-180);
        person_2.setY(-180);
        person_3.setX(-180);
        person_3.setY(-180);
        person_4.setX(-180);
        person_4.setY(-180);
        bad_guy.setX(-180);
        bad_guy.setY(-180);

        scoreLabel.setText("Score : 0");


    }

    public void changePos(){

        hitCheck();

        //Person 1
        person_1X -= 12;
        if (person_1X < 0){
            person_1X = screenWidth + 20;
            person_1Y = (int) Math.floor(Math.random() * (frameHeight - person_1.getHeight()));
        }
        person_1.setX(person_1X);
        person_1.setY(person_1Y);

        //Person 2
        person_2X -= 16;
        if (person_2X < 0){
            person_2X = screenWidth + 15;
            person_2Y = (int) Math.floor(Math.random() * (frameHeight - person_2.getHeight()));
        }
        person_2.setX(person_2X);
        person_2.setY(person_2Y);

        //Person 3
        person_3X -= 20;
        if (person_3X < 0){
            person_3X = screenWidth + 5000;
            person_3Y = (int) Math.floor(Math.random() * (frameHeight - person_3.getHeight()));
        }
        person_3.setX(person_3X);
        person_3.setY(person_3Y);

        //Person 4
        person_4X -= 24;
        if (person_4X < 0){
            person_4X = screenWidth + 30;
            person_4Y = (int) Math.floor(Math.random() * (frameHeight - person_4.getHeight()));
        }
        person_4.setX(person_4X);
        person_4.setY(person_4Y);

        //bad guy
        bad_guyX -= 28;
        if (bad_guyX < 0){
            bad_guyX = screenWidth + 10;
            bad_guyY = (int) Math.floor(Math.random() * (frameHeight - bad_guy.getHeight()));
        }
        bad_guy.setX(bad_guyX);
        bad_guy.setY(bad_guyY);

        //Move Tram
        if (action_flag){
            //Touching
            tramY -= 20;
        }
        else {
            //Releasing
            tramY += 20;
        }

        //check tram position
        if (tramY < 0) tramY = 0;
        if (tramY > frameHeight - tramHeight) tramY = frameHeight - tramHeight;
        tram.setY(tramY);
        scoreLabel.setText("Score : "+ score);
    }

    public void hitCheck() {

        //If the center of the person is in the tram, it counts as a hit

        //Person 1
        int person1CenterX = person_1X + person_1.getWidth() / 2;
        int person1CenterY = person_1Y + person_1.getHeight() / 2;

        if (0 <= person1CenterX && person1CenterX <= tramHeight &&
                tramY <= person1CenterY && person1CenterY <= tramY + tramHeight ){
            score += 10;
            person_1X = -10;
        }

        //Person 2
        int person2CenterX = person_2X + person_2.getWidth() / 2;
        int person2CenterY = person_2Y + person_2.getHeight() / 2;

        if (0 <= person2CenterX && person2CenterX <= tramHeight &&
                tramY <= person2CenterY && person2CenterY <= tramY + tramHeight ){
            score += 10;
            person_2X = -10;
        }

        //Person 3
        int person3CenterX = person_3X + person_3.getWidth() / 2;
        int person3CenterY = person_3Y + person_3.getHeight() / 2;

        if (0 <= person3CenterX && person3CenterX <= tramHeight &&
                tramY <= person3CenterY && person3CenterY <= tramY + tramHeight ){
            score += 20;
            person_3X = -10;
        }


        //Person 4
        int person4CenterX = person_4X + person_4.getWidth() / 2;
        int person4CenterY = person_4Y + person_4.getHeight() / 2;

        if (0 <= person4CenterX && person4CenterX <= tramHeight &&
                tramY <= person4CenterY && person4CenterY <= tramY + tramHeight ){
            score += 20;
            person_4X = -10;
        }

        //bad guy
        int badCenterX = bad_guyX + bad_guy.getWidth() / 2;
        int badCenterY = bad_guyY + bad_guy.getHeight() / 2;

        if (0 <= badCenterX && badCenterX <= tramHeight &&
                tramY <= badCenterY && badCenterY <= tramY + tramHeight ){

            //Stop Timer
            timer.cancel();
            timer = null;

            //Show result
            Intent intent = new Intent(GameActivity.this, GameResult.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }
     }

    public boolean onTouchEvent(MotionEvent me){

        if (start_flag == false){
            start_flag = true;

            FrameLayout frame = (FrameLayout)  findViewById(R.id.frame);
            frameHeight = frame.getHeight();
            tramY = (int)tram.getY();
            tramHeight = tram.getHeight();

            startLabel.setVisibility(View.GONE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);
        }
        else {
            if(me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flag = true;
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flag = false;
            }
        }

        return true;
    }
}

