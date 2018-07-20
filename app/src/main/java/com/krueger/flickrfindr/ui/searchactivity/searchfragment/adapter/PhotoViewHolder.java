package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.krueger.flickrfindr.R;
import com.krueger.flickrfindr.models.Photo;

import butterknife.BindView;
import butterknife.ButterKnife;

class PhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgThumbnail)
    ImageView imgThumbnail;

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.loadingProgress)
    ContentLoadingProgressBar progressBar;

    private RequestManager glideRequest;
    private PhotoClickListener clickListener;

    PhotoViewHolder(View view, RequestManager glideRequest, PhotoClickListener clickListener) {
        super(view);
        this.glideRequest = glideRequest;
        this.clickListener = clickListener;
        ButterKnife.bind(this, view);
    }

    void bind(Photo photo) {
        loadThumbnail(photo);
        txtTitle.setText(photo.title());
        imgThumbnail.setOnClickListener(v -> clickListener.photoClicked(photo));
    }

    private void loadThumbnail(Photo photo) {
        displayLoading();

        glideRequest
                .load(photo.thumbnailUrl())
                .apply(RequestOptions.centerCropTransform().error(R.mipmap.error_round))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        hideLoading();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        hideLoading();
                        return false;
                    }
                })
                .into(imgThumbnail);
    }

    private void displayLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

}
