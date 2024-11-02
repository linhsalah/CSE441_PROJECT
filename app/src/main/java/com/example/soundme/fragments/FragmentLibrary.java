package com.example.soundme.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soundme.MyApplication;
import com.example.soundme.R;
//import com.example.soundme.activities.PlaylistActivity;
import com.example.soundme.adapters.SongAdapter;
import com.example.soundme.constant.Constant;
import com.example.soundme.constant.GlobalFuntion;
import com.example.soundme.databinding.FragmentLibraryBinding;
import com.example.soundme.listeners.IOnClickSongItemListener;
import com.example.soundme.models.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentLibrary extends Fragment {
    private List<Song> mListSong;
    private FragmentLibraryBinding mFragmentLibraryBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentLibraryBinding = FragmentLibraryBinding.inflate(inflater, container, false);
        return mFragmentLibraryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListSongFromFirebase();
    }

    private void getListSongFromFirebase() {
        if (getActivity() == null) return;
        MyApplication.get(getActivity()).getSongsDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mListSong = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Song song = dataSnapshot.getValue(Song.class);
                            if (song == null) continue;
                            mListSong.add(0, song);
                        }
                        displayListFavoriteSongs();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
                    }
                });
    }

    private void displayListFavoriteSongs() {
        if (getActivity() == null || mListSong == null) return;

        List<Song> list = getListFavoriteSongs();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentLibraryBinding.rcvFavoriteSongs.setLayoutManager(linearLayoutManager);

        SongAdapter songAdapter = new SongAdapter(list, new IOnClickSongItemListener() {
            @Override
            public void onClickItemSong(Song song) {
                // goToSongDetail(song);
            }

            @Override
            public void onClickFavoriteSong(Song song, boolean favorite) {
                 GlobalFuntion.onClickFavoriteSong(getActivity(), song, favorite);
            }


            @Override
            public void onClickMoreOptions(Song song) {
//                 GlobalFuntion.handleClickMoreOptions(getActivity(), song);
            }
        });
        mFragmentLibraryBinding.rcvFavoriteSongs.setAdapter(songAdapter);
    }

    private List<Song> getListFavoriteSongs() {
        List<Song> list = new ArrayList<>();
        if (mListSong == null || mListSong.isEmpty()) {
            return list;
        }
        for (Song song : mListSong) {
            if (GlobalFuntion.isFavoriteSong(song) && list.size() < Constant.MAX_COUNT_FAVORITE) {
                list.add(song);
            }
        }
        return list;
    }
}
