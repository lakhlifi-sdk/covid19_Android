package com.lakhlifi.ml_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private TextView txtMessage;

    // Animation
    private Animation animFadein,animFadeinText;
    private CircleImageView imageCovidLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this code will pause the app for 2s
        Handler handler=new Handler();
        txtMessage = (TextView) findViewById(R.id.textLogo);
        imageCovidLogo =(CircleImageView) findViewById(R.id.imgCovidLogo);
        // load the animation
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        animFadeinText = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        txtMessage.startAnimation(animFadeinText);
        imageCovidLogo.startAnimation(animFadein);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                finish();
            }
        },1500);
    }
}