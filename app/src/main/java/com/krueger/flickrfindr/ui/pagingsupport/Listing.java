package com.krueger.flickrfindr.ui.pagingsupport;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import com.krueger.flickrfindr.utils.NetworkState;

import io.reactivex.functions.Action;

// This is ported from: com.android.example.paging.pagingwithnetwork.reddit.repository.Listing
// from https://github.com/googlesamples/android-architecture-components/tree/master/PagingWithNetworkSample

public class Listing<T> {
    private final LiveData<PagedList<T>> pagedList;
    private final LiveData<NetworkState> networkState;
    private final LiveData<NetworkState> refreshState;
    private final Action refreshAction;
    private final Action retryAction;

    public Listing(
            LiveData<PagedList<T>> pagedList
            , LiveData<NetworkState> networkState
            , LiveData<NetworkState> refreshState
            , Action refreshAction
            , Action retryAction) {

        this.pagedList = pagedList;
        this.networkState = networkState;
        this.refreshState = refreshState;
        this.refreshAction = refreshAction;
        this.retryAction = retryAction;
    }

    public LiveData<PagedList<T>> getPagedList() {
        return pagedList;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<NetworkState> getRefreshState() {
        return refreshState;
    }

    public Action getRefreshAction() {
        return refreshAction;
    }

    public Action getRetryAction() {
        return retryAction;
    }
}
