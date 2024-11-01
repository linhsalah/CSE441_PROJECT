package com.example.soundme.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soundme.R;
import com.example.soundme.databinding.ActivityPlayMusicBinding;


public class PlayMusicActivity extends AppCompatActivity {

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
//        MusicViewPagerAdapter musicViewPagerAdapter = new MusicViewPagerAdapter(this);
//        mActivityPlayMusicBinding.viewpager2.setAdapter(musicViewPagerAdapter);
        mActivityPlayMusicBinding.indicator3.setViewPager(mActivityPlayMusicBinding.viewpager2);
        mActivityPlayMusicBinding.viewpager2.setCurrentItem(1);
    }

    /*public void downloadSong(Song song) {
        mSong = song;
        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_PERMISSION_CODE);
            } else {
                GlobalFuntion.startDownloadFile(this, mSong);
            }
        } else {
            GlobalFuntion.startDownloadFile(this, mSong);
        }
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
    }*/
}