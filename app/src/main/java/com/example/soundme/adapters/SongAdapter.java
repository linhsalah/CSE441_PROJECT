package com.example.soundme.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundme.databinding.ItemSongBinding;
import com.example.soundme.listener.IOnClickSongItemListener;
import com.example.soundme.models.Song;
import com.example.soundme.utils.GlideUtils;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private final List<Song> mListSongs;
    public final IOnClickSongItemListener iOnClickSongItemListener;

    public SongAdapter(List<Song> mListSongs, IOnClickSongItemListener mIOnClickSongItemListener) {
        this.mListSongs = mListSongs;
        this.iOnClickSongItemListener = mIOnClickSongItemListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSongBinding itemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SongViewHolder(itemSongBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = mListSongs.get(position);
        if (song == null){
            return;
        }
        GlideUtils.loadUrl(song.getImage(), holder.mItemSongBinding.imgSong);
        holder.mItemSongBinding.tvSongName.setText(song.getTitle());
        holder.mItemSongBinding.tvArtist.setText(song.getArtist());
//        holder.mItemSongBinding.tvCountListen.setText(String.valueOf(song.getCount()));

//        boolean isFavorite = GlobalFuntion.isFavoriteSong(song);
//        if (isFavorite) {
//            holder.mItemSongBinding.imgFavorite.setImageResource(R.drawable.ic_favorite);
//        } else {
//            holder.mItemSongBinding.imgFavorite.setImageResource(R.drawable.ic_unfavorite);
//        }

//        holder.mItemSongBinding.imgFavorite.setOnClickListener(v -> iOnClickSongItemListener.onClickFavoriteSong(song, !isFavorite));
        holder.mItemSongBinding.imgMoreOption.setOnClickListener(v -> iOnClickSongItemListener.onClickMoreOptions(song));
        holder.mItemSongBinding.layoutSongInfo.setOnClickListener(v -> iOnClickSongItemListener.onClickItemSong(song));
    }

    @Override
    public int getItemCount() {
        return null == mListSongs ? 0 : mListSongs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        private final ItemSongBinding mItemSongBinding;

        public SongViewHolder(ItemSongBinding itemSongBinding) {
            super(itemSongBinding.getRoot());
            this.mItemSongBinding = itemSongBinding;
        }
    }
}