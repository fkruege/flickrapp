package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krueger.flickrfindr.R;
import com.krueger.flickrfindr.models.Photo;
import com.krueger.flickrfindr.utils.NetworkState;

public class PhotoResultsAdapter extends PagedListAdapter<Photo, RecyclerView.ViewHolder> {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private NetworkState networkState;

//    private PhotoResults photoResults;
    private Fragment fragment;

    public PhotoResultsAdapter(Fragment fragment) {
        super(PhotoDiffUtil.DIFF_CALLBACK);
        this.fragment = fragment;
//        photoResults = PhotoResults.builder()
//                .setPhotoList(Collections.<Photo>emptyList())
//                .setTotalPhotos(0)
//                .setTotalPages(0)
//                .setPageNo(0)
//                .build();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == TYPE_PROGRESS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_network_loading, parent, false);
            NetworkStateItemViewHolder viewHolder = new NetworkStateItemViewHolder(view);
            return viewHolder;

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_photo, parent, false);
            PhotoViewHolder viewHolder = new PhotoViewHolder(fragment, view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof PhotoViewHolder) {
            PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;
            Photo photo = getItem(position);
            photoViewHolder.bind(photo);
        } else {
            ((NetworkStateItemViewHolder) holder).bindView(networkState);
        }


    }

    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
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

//    @Override
//    public int getItemCount() {
//        return photoResults.photoList().size();
//    }

//    public void update(PhotoResults photoResults) {
//        this.photoResults = photoResults;
//    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

}
