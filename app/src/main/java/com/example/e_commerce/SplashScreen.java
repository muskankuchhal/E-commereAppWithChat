package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        img=(ImageView)findViewById(R.id.logo);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },2000);


    }
}
