package com.example.soundme.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundme.databinding.ItemCategoryBinding;
import com.example.soundme.listener.IOnClickCategoryItemListener;
import com.example.soundme.models.Category;
import com.example.soundme.utils.GlideUtils;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<Category> mListCategory;

    public CategoryAdapter(List<Category> list) {
        this.mListCategory = list;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding itemCategoryBinding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(itemCategoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mListCategory.get(position);
        if (category == null) {
            return;
        }
        GlideUtils.loadUrl(category.getImage(), holder.mItemCategoryBinding.imgCategory);
        holder.mItemCategoryBinding.tvCategory.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return null == mListCategory ? 0 : mListCategory.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemCategoryBinding mItemCategoryBinding;

        public CategoryViewHolder(ItemCategoryBinding itemCategoryBinding) {
            super(itemCategoryBinding.getRoot());
            this.mItemCategoryBinding = itemCategoryBinding;
        }
    }
}