package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.krueger.flickrfindr.R;
import com.krueger.flickrfindr.utils.NetworkState;

import butterknife.BindView;
import butterknife.ButterKnife;


class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txtErrorMsg)
    TextView txtErrorMsg;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    private View view;

    NetworkStateItemViewHolder(View view) {
        super(view);
        this.view = view;
        ButterKnife.bind(this, this.view);
    }

    void bindView(NetworkState networkState) {
        if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

        if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
            txtErrorMsg.setVisibility(View.VISIBLE);
            txtErrorMsg.setText(networkState.getMsg());
        } else {
            txtErrorMsg.setVisibility(View.GONE);
        }
    }
}
