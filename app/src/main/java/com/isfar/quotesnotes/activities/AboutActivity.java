package com.isfar.quotesnotes.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.appbar.MaterialToolbar;
import com.isfar.quotesnotes.R;
import com.isfar.quotesnotes.monetization.Admob;

public class AboutActivity extends AppCompatActivity {

    LinearLayout linearAbout;
    MaterialToolbar aboutToolbar;
    LinearLayout adContainerLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_scroll), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //===========================================
        linearAbout = findViewById(R.id.linear_about);
        aboutToolbar = findViewById(R.id.about_toolbar);
        adContainerLinear = findViewById(R.id.ad_container_linear);
        //===========================================


        //============== Back =================
        aboutToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // banner Ad....
        Admob.setBannerAd(adContainerLinear, AboutActivity.this);



    }//onCreate End...


}//Main End...