package com.example.soundme;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soundme.activities.MainActivity;
import com.example.soundme.activities.SignInActivity;
import com.example.soundme.constant.GlobalFuntion;
import com.example.soundme.databinding.ActivitySplashScreenBinding;
import com.example.soundme.prefs.DataStoreManager;
import com.example.soundme.utils.StringUtil;

public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding mActivitySplashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySplashBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(mActivitySplashBinding.getRoot());
        Handler handler = new Handler();
        handler.postDelayed(this::goToActivity, 2000);
    }

    private void goToActivity() {
        if (DataStoreManager.getUser() != null
                && !StringUtil.isEmpty(DataStoreManager.getUser().getEmail())) {
            GlobalFuntion.startActivity(this, MainActivity.class);
        } else {
            GlobalFuntion.startActivity(this, SignInActivity.class);
        }
        finish();
    }
}