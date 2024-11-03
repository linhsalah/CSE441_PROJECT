package com.example.soundme.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.soundme.fragments.FragmentDetail;
import com.example.soundme.fragments.FragmentFullPlayer;

public class MusicViewPagerAdapter extends FragmentStateAdapter {
    public MusicViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new FragmentDetail();
        }
        return new FragmentFullPlayer();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
