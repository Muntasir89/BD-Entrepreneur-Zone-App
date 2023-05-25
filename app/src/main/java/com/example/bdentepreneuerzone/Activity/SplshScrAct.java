package com.example.bdentepreneuerzone.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bdentepreneuerzone.R;

public class SplshScrAct extends AppCompatActivity {
    ImageView logo;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splshscr);

        logo = findViewById(R.id.logo);
        title = findViewById(R.id.app_title);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.expand);
        logo.startAnimation(animation);
        title.startAnimation(animation);

        //Setting splash Screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), HomeAct.class));
                finish();
            }
        }, 3500);
    }
}