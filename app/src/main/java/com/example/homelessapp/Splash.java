package com.example.homelessapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    ImageView logo;
    TextView appName;
    Animation top,bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.title);

        //Setting the animation for logo and app name
        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);

        logo.setAnimation(top);
        appName.setAnimation(bottom);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                //Checking if there is logged in user or not
                SharedPreferences pref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                if(!pref.contains("user_key")){
                    startActivity(new Intent(Splash.this, MainActivity.class));
                }else{
                    startActivity(new Intent(Splash.this, MainMenu.class));
                }
                finish();
            }
        },2500);

    }
    //TODO change color F9AA33 to call button
}