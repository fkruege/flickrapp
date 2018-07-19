package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.krueger.flickrfindr.R;
import com.krueger.flickrfindr.models.Photo;
import com.krueger.flickrfindr.utils.NetworkState;

import io.reactivex.functions.Action;

public class PhotoResultsAdapter extends PagedListAdapter<Photo, RecyclerView.ViewHolder> {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;
    private PhotoClickListener photoClickListener;
    private final RequestManager glideRequest;
    private final Action retryCallback;

    private NetworkState networkState;

    public PhotoResultsAdapter(PhotoClickListener photoClickListener, RequestManager glideRequest, Action retryCallback) {
        super(PhotoDiffUtil.DIFF_CALLBACK);
        this.photoClickListener = photoClickListener;
        this.glideRequest = glideRequest;
        this.retryCallback = retryCallback;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PhotoViewHolder) {
            PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;
            Photo photo = getItem(position);
            photoViewHolder.bind(photo);
        } else {
            ((NetworkStateItemViewHolder) holder).bindView(networkState);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_PROGRESS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_network_loading, parent, false);
            return new NetworkStateItemViewHolder(view);

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_photo, parent, false);
            return new PhotoViewHolder(view, glideRequest, photoClickListener);
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        if (hasExtraRow()) {
            return itemCount + 1;
        } else {
            return itemCount;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }


    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }


    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(super.getItemCount());
            } else {
                notifyItemInserted(super.getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

}
