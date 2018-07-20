package com.krueger.flickrfindr.ui.pagingsupport;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

import com.krueger.flickrfindr.utils.NetworkState;

// This is ported from: com.android.example.paging.pagingwithnetwork.reddit.repository.Listing
// from https://github.com/googlesamples/android-architecture-components/tree/master/PagingWithNetworkSample

public class Listing<T> {
    private final LiveData<PagedList<T>> pagedList;
    private final LiveData<NetworkState> networkState;

    public Listing(
            LiveData<PagedList<T>> pagedList
            , LiveData<NetworkState> networkState
    ) {
        this.pagedList = pagedList;
        this.networkState = networkState;
    }

    public LiveData<PagedList<T>> getPagedList() {
        return pagedList;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

}
