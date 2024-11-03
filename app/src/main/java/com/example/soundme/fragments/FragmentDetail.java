package com.example.soundme.fragments;

import static com.example.soundme.service.MusicService.deleteSongFromPlaylist;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.soundme.R;
import com.example.soundme.adapters.SongPlayingAdapter;
import com.example.soundme.constant.Constant;
import com.example.soundme.constant.GlobalFuntion;
import com.example.soundme.databinding.FragmentDetailBinding;
import com.example.soundme.listener.IOnClickSongPlayingItemListener;
import com.example.soundme.service.MusicService;

public class FragmentDetail extends Fragment {
    private FragmentDetailBinding mFragmentDetailBinding;
    private SongPlayingAdapter mSongPlayingAdapter;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateStatusListSongPlaying();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentDetailBinding = FragmentDetailBinding.inflate(inflater, container, false);

        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver,
                    new IntentFilter(Constant.CHANGE_LISTENER));
        }
        displayListSongPlaying();

        return mFragmentDetailBinding.getRoot();
    }

    private void displayListSongPlaying() {
        if (getActivity() == null || MusicService.mListSongPlaying == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentDetailBinding.rcvData.setLayoutManager(linearLayoutManager);

        mSongPlayingAdapter = new SongPlayingAdapter(MusicService.mListSongPlaying,
                new IOnClickSongPlayingItemListener() {
                    @Override
                    public void onClickItemSongPlaying(int position) {
                        clickItemSongPlaying(position);
                    }

                    @Override
                    public void onClickRemoveFromPlaylist(int position) {
                        deleteSongFromPlaylist(position);
                    }
                });
        mFragmentDetailBinding.rcvData.setAdapter(mSongPlayingAdapter);

        updateStatusListSongPlaying();
    }

    private void clickItemSongPlaying(int position) {
        MusicService.isPlaying = false;
        GlobalFuntion.startMusicService(getActivity(), Constant.PLAY, position);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateStatusListSongPlaying() {
        if (getActivity() == null || MusicService.mListSongPlaying == null || MusicService.mListSongPlaying.isEmpty()) {
            return;
        }
        for (int i = 0; i < MusicService.mListSongPlaying.size(); i++) {
            MusicService.mListSongPlaying.get(i).setPlaying(i == MusicService.mSongPosition);
        }
        mSongPlayingAdapter.notifyDataSetChanged();
    }


}