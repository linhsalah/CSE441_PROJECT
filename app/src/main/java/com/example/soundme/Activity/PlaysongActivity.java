package com.example.soundme.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PlaysongActivity extends AppCompatActivity {

        private static final int REQUEST_PERMISSION_CODE = 10;
        private Song mSong;
        private ActivityPlayMusicBinding mActivityPlayMusicBinding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mActivityPlayMusicBinding = ActivityPlayMusicBinding.inflate(getLayoutInflater());
            setContentView(mActivityPlayMusicBinding.getRoot());

            initToolbar();
            initUI();
        }

        private void initToolbar() {
            mActivityPlayMusicBinding.toolbar.imgLeft.setImageResource(R.drawable.ic_back_white);
            mActivityPlayMusicBinding.toolbar.tvTitle.setText(R.string.music_player);
            mActivityPlayMusicBinding.toolbar.layoutPlayAll.setVisibility(View.GONE);
            mActivityPlayMusicBinding.toolbar.imgLeft.setOnClickListener(v -> onBackPressed());
        }

        private void initUI() {
            MusicViewPagerAdapter musicViewPagerAdapter = new MusicViewPagerAdapter(this);
            mActivityPlayMusicBinding.viewpager2.setAdapter(musicViewPagerAdapter);
            mActivityPlayMusicBinding.indicator3.setViewPager(mActivityPlayMusicBinding.viewpager2);
            mActivityPlayMusicBinding.viewpager2.setCurrentItem(1);
        }


        @SuppressLint("MissingSuperCall")
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
            if (requestCode == REQUEST_PERMISSION_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GlobalFuntion.startDownloadFile(this, mSong);
                } else {
                    Toast.makeText(this, getString(R.string.msg_permission_denied),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


