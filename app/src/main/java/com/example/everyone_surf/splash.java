package com.example.everyone_surf;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // מעבר אחרי 2.5 שניות
        new Handler().postDelayed(() -> {

            Intent intent = new Intent(splash.this, MainActivity.class);
            startActivity(intent);
            finish();

        }, 2500);
    }
}
