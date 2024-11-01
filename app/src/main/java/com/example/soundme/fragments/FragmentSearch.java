package com.example.soundme.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;

import com.example.soundme.MyApplication;
import com.example.soundme.R;
import com.example.soundme.activities.FullPlayerActivity;
import com.example.soundme.adapters.SongAdapter;
import com.example.soundme.constant.Constant;
import com.example.soundme.constant.GlobalFuntion;
import com.example.soundme.databinding.FragmentSearchBinding;
import com.example.soundme.listener.IOnClickSongItemListener;
import com.example.soundme.models.Song;
import com.example.soundme.service.MusicService;
import com.example.soundme.utils.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentSearch extends Fragment {
    private FragmentSearchBinding mFragmentSearchBinding;
    private FrameLayout search_frame;
    private List<Song> mListSong;
    private SongAdapter mSongAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false);

        initUi();
        initListener();
        getListSongFromFirebase("");

        return mFragmentSearchBinding.getRoot();
    }

    private void initListener() {
        mFragmentSearchBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    getListSongFromFirebase("");
                }
            }
        });
        mFragmentSearchBinding.imgSearch.setOnClickListener(view -> searchSong());

        mFragmentSearchBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchSong();
                return true;
            }
            return false;
        });
    }

    private void searchSong() {
        String strKey = mFragmentSearchBinding.edtSearchName.getText().toString().trim();
        getListSongFromFirebase(strKey);
        GlobalFuntion.hideSoftKeyboard(getActivity());
    }

    private void getListSongFromFirebase(String key) {
        if (getActivity() == null) return;
        MyApplication.get(getActivity()).getSongsDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        resetListData();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Song song = dataSnapshot.getValue(Song.class);
                            if (song == null) return;
                            if (StringUtil.isEmpty(key)) {
                                mListSong.add(0, song);
                            } else {
                                if (GlobalFuntion.getTextSearch(song.getTitle()).toLowerCase().trim()
                                        .contains(GlobalFuntion.getTextSearch(key).toLowerCase().trim())) {
                                    mListSong.add(0, song);
                                }
                            }
                        }
                        if (mSongAdapter != null) mSongAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
                    }
                });
    }

    private void resetListData() {
        if (mListSong == null) {
            mListSong = new ArrayList<>();
        } else {
            mListSong.clear();
        }
    }

    private void initUi() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentSearchBinding.rcvSearchResult.setLayoutManager(linearLayoutManager);
        mListSong = new ArrayList<>();
        mSongAdapter = new SongAdapter(mListSong, new IOnClickSongItemListener() {
            @Override
            public void onClickItemSong(Song song) {
                goToSongDetail(song);
            }

            @Override
            public void onClickFavoriteSong(Song song, boolean favorite) {
//                GlobalFuntion.onClickFavoriteSong(getActivity(), song, favorite);
            }

            @Override
            public void onClickMoreOptions(Song song) {
//                GlobalFuntion.handleClickMoreOptions(getActivity(), song);
            }
        });
        mFragmentSearchBinding.rcvSearchResult.setAdapter(mSongAdapter);
    }

    private void goToSongDetail(Song song) {
        MusicService.clearListSongPlaying();
        MusicService.mListSongPlaying.add(song);
        MusicService.isPlaying = false;
        GlobalFuntion.startMusicService(getActivity(), Constant.PLAY, 0);
        GlobalFuntion.startActivity(getActivity(), FullPlayerActivity.class);
    }
}