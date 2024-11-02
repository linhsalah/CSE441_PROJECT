package com.example.soundme.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soundme.R;

public class SettingsActivity extends AppCompatActivity {
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        addControl();
        addEvent();
    }

    private void addControl() {
        imgBack = findViewById(R.id.imgBack);
    }

    private void addEvent() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.animation_finish_enter, R.anim.animation_finish_leave);
    }
}
