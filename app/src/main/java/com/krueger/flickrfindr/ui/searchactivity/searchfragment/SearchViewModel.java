package com.krueger.flickrfindr.ui.searchactivity.searchfragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.krueger.flickrfindr.models.Photo;
import com.krueger.flickrfindr.ui.pagingsupport.Listing;
import com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter.PagedPhotoRepository;
import com.krueger.flickrfindr.utils.NetworkState;

import javax.inject.Inject;

import timber.log.Timber;

import static android.arch.lifecycle.Transformations.map;
import static android.arch.lifecycle.Transformations.switchMap;

// For paging
// https://proandroiddev.com/8-steps-to-implement-paging-library-in-android-d02500f7fffe
// https://codelabs.developers.google.com/codelabs/android-paging/index.html#8

public class SearchViewModel extends ViewModel {

    private PagedPhotoRepository pagedPhotoRepository;

    final MutableLiveData<String> query = new MutableLiveData<>();

    final LiveData<Listing<Photo>> searchResults = map(query, input -> pagedPhotoRepository.searchPhotos(input));

    final LiveData<PagedList<Photo>> currentPhotoPagedList = switchMap(searchResults, input -> input.getPagedList());

    final LiveData<NetworkState> networkState = switchMap(searchResults, input -> {
        return input.getNetworkState();
    });
    final LiveData<NetworkState> refreshState = switchMap(searchResults, input -> input.getRefreshState());


    @Inject
    SearchViewModel(PagedPhotoRepository pagedPhotoRepository) {
        this.pagedPhotoRepository = pagedPhotoRepository;
    }

    void refresh() {
        Listing<Photo> photoListing = getPhotoListing();
        if (photoListing != null && photoListing.getRefreshAction() != null) {
            try {
                photoListing.getRefreshAction().run();
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }

    boolean showNewSearch(String newQuery) {
        if (query.getValue() != null && query.getValue().equals(newQuery)) {
            return false;
        }

        query.setValue(newQuery);
        return true;
    }

    void retry() {
        Listing<Photo> photoListing = getPhotoListing();
        if (photoListing != null && photoListing.getRetryAction() != null) {
            try {
                photoListing.getRetryAction().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    String currentQuery() {
        return query.getValue();
    }


    private Listing<Photo> getPhotoListing() {
        if (searchResults != null && searchResults.getValue() != null) {
            return searchResults.getValue();
        }
        return null;
    }
}
