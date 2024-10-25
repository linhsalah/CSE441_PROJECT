package com.example.soundme.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundme.databinding.ItemArtistHorizontalBinding;
import com.example.soundme.listener.IOnClickArtistItemListener;
import com.example.soundme.models.Artist;
import com.example.soundme.utils.GlideUtils;

import java.util.List;

public class ArtistHorizontalAdapter extends RecyclerView.Adapter<ArtistHorizontalAdapter.ArtistHorizontalViewHolder> {

    private final List<Artist> mListArtist;
    public final IOnClickArtistItemListener iOnClickArtistItemListener;

    public ArtistHorizontalAdapter(List<Artist> list, IOnClickArtistItemListener listener) {
        this.mListArtist = list;
        this.iOnClickArtistItemListener = listener;
    }

    @NonNull
    @Override
    public ArtistHorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArtistHorizontalBinding itemArtistHorizontalBinding = ItemArtistHorizontalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ArtistHorizontalViewHolder(itemArtistHorizontalBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistHorizontalViewHolder holder, int position) {
        Artist artist = mListArtist.get(position);
        if (artist == null) return;
        GlideUtils.loadUrl(artist.getImage(), holder.mItemArtistHorizontalBinding.imgArtist);
        holder.mItemArtistHorizontalBinding.tvArtist.setText(artist.getName());

        holder.mItemArtistHorizontalBinding.layoutItem.setOnClickListener(v -> iOnClickArtistItemListener.onClickItemArtist(artist));
    }

    @Override
    public int getItemCount() {
        return null == mListArtist ? 0 : mListArtist.size();
    }

    public static class ArtistHorizontalViewHolder extends RecyclerView.ViewHolder {

        private final ItemArtistHorizontalBinding mItemArtistHorizontalBinding;

        public ArtistHorizontalViewHolder(ItemArtistHorizontalBinding itemArtistHorizontalBinding) {
            super(itemArtistHorizontalBinding.getRoot());
            this.mItemArtistHorizontalBinding = itemArtistHorizontalBinding;
        }
    }
}
