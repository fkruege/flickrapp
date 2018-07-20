package com.krueger.flickrfindr.ui.searchactivity.displayphotofragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.krueger.flickrfindr.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DisplayPhotoFragment extends DialogFragment {

    private static final String KEY_URL = "url";

    @BindView(R.id.fullImage)
    ImageView imgFull;

    @BindView(R.id.fullImageLoadingProgress)
    ContentLoadingProgressBar progressBar;

    private Unbinder unbinder;


    public static DisplayPhotoFragment newInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);

        DisplayPhotoFragment fragment = new DisplayPhotoFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.display_photo_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        loadImage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void loadImage() {
        String imageUrl = getImageUrl();
        loadUrlIntImage(imageUrl);
    }

    private void loadUrlIntImage(String url) {
        displayLoading();

        Glide.with(this)
                .load(url)
                .apply(RequestOptions.noTransformation().error(R.mipmap.error_round))
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
                .into(imgFull);
    }


    private void displayLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private String getImageUrl() {
        Bundle arguments = this.getArguments();
        if (arguments == null) {
            return "";
        }

        if (arguments.getString(KEY_URL) == null) {
            return "";
        }

        return arguments.getString(KEY_URL);

    }

}
