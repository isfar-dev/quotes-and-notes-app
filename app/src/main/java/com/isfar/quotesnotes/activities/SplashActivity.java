package com.isfar.quotesnotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.isfar.quotesnotes.R;

public class SplashActivity extends AppCompatActivity {

    Animation Splash_top,Splash_bottom;
    CardView splash_logo;
    TextView splash_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        splash_logo = findViewById(R.id.splash_logo);
        splash_name = findViewById(R.id.splash_name);


        Splash_top = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_anim);
        Splash_bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top_anim);


        splash_logo.setAnimation(Splash_top);
        splash_name.setAnimation(Splash_bottom);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1500);



    }// onCreate End....

}// Main Class End....