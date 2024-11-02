package com.example.soundme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.soundme.R;

public class SplashScreenActivity extends AppCompatActivity {
    private String LOG_TAG = "SplashScreenActivity";
    private static final int SHOWING_INTERVAL = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        openMainActivity();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void openMainActivity() {
        Log.d(LOG_TAG, "openMainActivity");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "run");
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SHOWING_INTERVAL);
    }
}