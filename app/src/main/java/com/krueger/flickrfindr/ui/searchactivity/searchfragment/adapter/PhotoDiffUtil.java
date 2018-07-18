package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.krueger.flickrfindr.models.Photo;

public class PhotoDiffUtil {


    public static DiffUtil.ItemCallback<Photo> DIFF_CALLBACK = new DiffUtil.ItemCallback<Photo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem.id().equals(newItem.id());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem.equals(newItem);
        }
    };
}

