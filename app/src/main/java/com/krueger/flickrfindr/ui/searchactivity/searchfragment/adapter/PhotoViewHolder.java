package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.krueger.flickrfindr.R;
import com.krueger.flickrfindr.models.Photo;

import butterknife.BindView;
import butterknife.ButterKnife;

class PhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgThumbnail)
    ImageView imgThumbnail;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    private Fragment fragment;
    private View view;


    PhotoViewHolder(Fragment fragment, View view) {
        super(view);
        this.fragment = fragment;
        this.view = view;
        ButterKnife.bind(this, this.view);
    }

    void bind(Photo photo) {
        Glide.with(fragment)
                .load(photo.thumbnailUrl())
                .into(imgThumbnail);

        txtTitle.setText(photo.title());
    }

}
