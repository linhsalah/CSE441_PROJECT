package com.example.soundme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soundme.databinding.FragmentPlaySongBinding;

import java.util.Timer;

public class PlaySongFragment extends Fragment implements View.OnClickListener {

    private FragmentPlaySongBinding mFragmentPlaySongBinding;
    private Timer mTimer;
    private int mAction;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentPlaySongBinding = FragmentPlaySongBinding.inflate(inflater, container, false);

        return mFragmentPlaySongBinding.getRoot();
    }

    @Override
    public void onClick(View view) {

    }
}