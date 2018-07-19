package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.krueger.flickrfindr.R;
import com.krueger.flickrfindr.models.Photo;

import butterknife.BindView;
import butterknife.ButterKnife;

class PhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgThumbnail)
    ImageView imgThumbnail;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    private RequestManager glideRequest;
    private PhotoClickListener clickListener;

    PhotoViewHolder(View view, RequestManager glideRequest, PhotoClickListener clickListener) {
        super(view);
        this.glideRequest = glideRequest;
        this.clickListener = clickListener;
        ButterKnife.bind(this, view);
    }

    void bind(Photo photo) {
        glideRequest.load(photo.thumbnailUrl()).into(imgThumbnail);
        txtTitle.setText(photo.title());

        imgThumbnail.setOnClickListener(v -> clickListener.photoClicked(photo));
    }

}
