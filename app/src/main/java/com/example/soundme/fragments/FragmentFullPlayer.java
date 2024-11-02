package com.example.soundme.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.soundme.MyApplication;
import com.example.soundme.R;
import com.example.soundme.constant.Constant;
import com.example.soundme.constant.GlobalFuntion;
import com.example.soundme.databinding.FragmentFullPlayerBinding;
import com.example.soundme.models.Song;
import com.example.soundme.service.MusicService;
import com.example.soundme.utils.AppUtil;
import com.example.soundme.utils.GlideUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("NonConstantResourceId")
public class FragmentFullPlayer extends Fragment implements View.OnClickListener {
    private FragmentFullPlayerBinding mFragmentPlaySongBinding;
    private Timer mTimer;
    private int mAction;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mAction = intent.getIntExtra(Constant.MUSIC_ACTION, 0);
            handleMusicAction();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentPlaySongBinding = FragmentFullPlayerBinding.inflate(inflater, container, false);

        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver,
                    new IntentFilter(Constant.CHANGE_LISTENER));
        }
        initControl();
        showInforSong();
        updateStatusShuffleButton();
        updateStatusRepeatButton();
        mAction = MusicService.mAction;
        handleMusicAction();

        return mFragmentPlaySongBinding.getRoot();
    }

    private void initControl() {
        mTimer = new Timer();

        mFragmentPlaySongBinding.imgShuffle.setOnClickListener(this);
        mFragmentPlaySongBinding.imgRepeat.setOnClickListener(this);
        mFragmentPlaySongBinding.imgPrevious.setOnClickListener(this);
        mFragmentPlaySongBinding.imgPlay.setOnClickListener(this);
        mFragmentPlaySongBinding.imgNext.setOnClickListener(this);
        mFragmentPlaySongBinding.imgDownload.setOnClickListener(this);

        mFragmentPlaySongBinding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicService.mPlayer.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
    }

    private void showInforSong() {
        if (MusicService.mListSongPlaying == null || MusicService.mListSongPlaying.isEmpty()) {
            return;
        }
        Song currentSong = MusicService.mListSongPlaying.get(MusicService.mSongPosition);
        mFragmentPlaySongBinding.tvSongName.setText(currentSong.getTitle());
        mFragmentPlaySongBinding.tvArtist.setText(currentSong.getArtist());
        GlideUtils.loadUrl(currentSong.getImage(), mFragmentPlaySongBinding.imgSong);
        listenerCountViewSong(currentSong.getId());
//        listenerFavoriteSong(currentSong.getId());
    }

    private void handleMusicAction() {
        if (Constant.CANNEL_NOTIFICATION == mAction) {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
            return;
        }
        switch (mAction) {
            case Constant.PREVIOUS:
            case Constant.NEXT:
                stopAnimationPlayMusic();
                showInforSong();
                break;

            case Constant.PLAY:
                showInforSong();
                if (MusicService.isPlaying) {
                    startAnimationPlayMusic();
                }
                showSeekBar();
                showStatusButtonPlay();
                break;

            case Constant.PAUSE:
                stopAnimationPlayMusic();
                showSeekBar();
                showStatusButtonPlay();
                break;

            case Constant.RESUME:
                startAnimationPlayMusic();
                showSeekBar();
                showStatusButtonPlay();
                break;
        }
    }

    private void startAnimationPlayMusic() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mFragmentPlaySongBinding.imgSong.animate().rotationBy(360).withEndAction(this).setDuration(15000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        mFragmentPlaySongBinding.imgSong.animate().rotationBy(360).withEndAction(runnable).setDuration(15000)
                .setInterpolator(new LinearInterpolator()).start();
    }

    private void stopAnimationPlayMusic() {
        mFragmentPlaySongBinding.imgSong.animate().cancel();
    }

    public void showSeekBar() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(() -> {
                    if (MusicService.mPlayer == null) {
                        return;
                    }
                    mFragmentPlaySongBinding.tvTimeCurrent.setText(AppUtil.getTime(MusicService.mPlayer.getCurrentPosition()));
                    mFragmentPlaySongBinding.tvTimeMax.setText(AppUtil.getTime(MusicService.mLengthSong));
                    mFragmentPlaySongBinding.seekbar.setMax(MusicService.mLengthSong);
                    mFragmentPlaySongBinding.seekbar.setProgress(MusicService.mPlayer.getCurrentPosition());
                });
            }
        }, 0, 1000);
    }

    private void showStatusButtonPlay() {
        if (MusicService.isPlaying) {
            mFragmentPlaySongBinding.imgPlay.setImageResource(R.drawable.ic_pause_black);
        } else {
            mFragmentPlaySongBinding.imgPlay.setImageResource(R.drawable.ic_play_black);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id ==R.id.img_previous) {
            clickOnPrevButton();
        } else if (id ==R.id.img_play) {
            clickOnPlayButton();
        } else if (id ==R.id.img_next) {
            clickOnNextButton();
        } else if (id ==R.id.img_shuffle) {
            clickOnShuffleButton();
        } else if (id ==R.id.img_repeat) {
            clickOnRepeatButton();
        } else if (id ==R.id.img_download) {
//            clickOnDownloadSong();
        }
    }

    private void clickOnShuffleButton() {
        if (!MusicService.isShuffle) {
            MusicService.isShuffle = true;
            MusicService.isRepeat = false;
        } else {
            MusicService.isShuffle = false;
        }
        updateStatusShuffleButton();
        updateStatusRepeatButton();
    }

    private void clickOnRepeatButton() {
        if (!MusicService.isRepeat) {
            MusicService.isRepeat = true;
            MusicService.isShuffle = false;
        } else {
            MusicService.isRepeat = false;
        }
        updateStatusShuffleButton();
        updateStatusRepeatButton();
    }

    private void updateStatusShuffleButton() {
        if (MusicService.isShuffle) {
            mFragmentPlaySongBinding.imgShuffle.setImageResource(R.drawable.ic_shuffle_enable);
        } else {
            mFragmentPlaySongBinding.imgShuffle.setImageResource(R.drawable.ic_shuffle_disable);
        }
    }

    private void updateStatusRepeatButton() {
        if (MusicService.isRepeat) {
            mFragmentPlaySongBinding.imgRepeat.setImageResource(R.drawable.ic_repeat_one_enable);
        } else {
            mFragmentPlaySongBinding.imgRepeat.setImageResource(R.drawable.ic_repeat_disable);
        }
    }

    private void clickOnPrevButton() {
        GlobalFuntion.startMusicService(getActivity(), Constant.PREVIOUS, MusicService.mSongPosition);
    }

    private void clickOnNextButton() {
        GlobalFuntion.startMusicService(getActivity(), Constant.NEXT, MusicService.mSongPosition);
    }

    private void clickOnPlayButton() {
        if (MusicService.isPlaying) {
            GlobalFuntion.startMusicService(getActivity(), Constant.PAUSE, MusicService.mSongPosition);
        } else {
            GlobalFuntion.startMusicService(getActivity(), Constant.RESUME, MusicService.mSongPosition);
        }
    }

    private void listenerCountViewSong(int songId) {
        if (getActivity() == null) return;
        ValueEventListener countViewSongEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer currentCount = snapshot.getValue(Integer.class);
                if (currentCount != null) {
                    mFragmentPlaySongBinding.tvCountView.setText(String.valueOf(currentCount));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        MyApplication.get(getActivity()).getCountViewDatabaseReference(songId)
                .addValueEventListener(countViewSongEventListener);
    }

//    private void listenerFavoriteSong(int songId) {
//        if (getActivity() == null) return;
//        MyApplication.get(getActivity()).getSongDetailDatabaseReference(songId)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Song song = snapshot.getValue(Song.class);
//                        if (song == null) return;
//                        boolean isFavorite = GlobalFuntion.isFavoriteSong(song);
//                        if (isFavorite) {
//                            mFragmentPlaySongBinding.imgFavorite.setImageResource(R.drawable.ic_favorite);
//                        } else {
//                            mFragmentPlaySongBinding.imgFavorite.setImageResource(R.drawable.ic_unfavorite);
//                        }
//                        mFragmentPlaySongBinding.imgFavorite.setOnClickListener(
//                                view -> GlobalFuntion.onClickFavoriteSong(getActivity(), song, !isFavorite)
//                        );
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                    }
//                });
//    }

//    private void clickOnDownloadSong() {
//        Song currentSong = MusicService.mListSongPlaying.get(MusicService.mSongPosition);
//        PlayMusicActivity activity = (PlayMusicActivity) getActivity();
//        if (activity == null) return;
//        activity.downloadSong(currentSong);
//    }
}
