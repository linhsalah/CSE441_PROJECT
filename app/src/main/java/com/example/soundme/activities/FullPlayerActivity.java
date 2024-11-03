package com.example.soundme.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.soundme.adapters.MusicViewPagerAdapter;
import com.example.soundme.adapters.ViewPagerAdapter;
import com.example.soundme.databinding.ActivityFullPlayerBinding;
import com.example.soundme.fragments.FragmentDetail;
import com.example.soundme.fragments.FragmentFullPlayer;
import com.example.soundme.fragments.FragmentLyric;


public class FullPlayerActivity extends AppCompatActivity {

    private ActivityFullPlayerBinding mActivityFullPlayerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityFullPlayerBinding = ActivityFullPlayerBinding.inflate(getLayoutInflater());
        setContentView(mActivityFullPlayerBinding.getRoot());

        addControl();
        addEvent();
    }

    private void addControl() {
        MusicViewPagerAdapter musicViewPagerAdapter = new MusicViewPagerAdapter(this);
        mActivityFullPlayerBinding.vpFullPlayer.setAdapter(musicViewPagerAdapter);
        mActivityFullPlayerBinding.indicator3.setViewPager(mActivityFullPlayerBinding.vpFullPlayer);
        mActivityFullPlayerBinding.vpFullPlayer.setCurrentItem(1);
//        vpFullPlayer = findViewById(R.id.vpFullPlayer);
//        adapter = new ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//
//        fragmentFullPlayer = new FragmentFullPlayer();
//        fragmentDetail = new FragmentDetail();
//        fragmentLyric = new FragmentLyric();
//
//        adapter.addFragment(fragmentDetail);
//        adapter.addFragment(fragmentFullPlayer);
//        adapter.addFragment(fragmentLyric);
//
//        vpFullPlayer.setAdapter(adapter);
//
//        imgCollapse = findViewById(R.id.imgCollapse);
    }

    private void addEvent() {
        mActivityFullPlayerBinding.imgCollapse.setOnClickListener(v -> onBackPressed());
    }
}
