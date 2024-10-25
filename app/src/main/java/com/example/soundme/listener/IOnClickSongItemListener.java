package com.example.soundme.listener;

import com.example.soundme.models.Song;

public interface IOnClickSongItemListener {
    void onClickItemSong(Song song);
    void onClickFavoriteSong(Song song, boolean favorite);
    void onClickMoreOptions(Song song);
}
