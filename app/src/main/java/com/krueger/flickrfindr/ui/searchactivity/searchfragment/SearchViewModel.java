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

import static android.arch.lifecycle.Transformations.map;
import static android.arch.lifecycle.Transformations.switchMap;

public class SearchViewModel extends ViewModel {

    private PagedPhotoRepository pagedPhotoRepository;

    private final MutableLiveData<String> query = new MutableLiveData<>();

    private final LiveData<Listing<Photo>> searchResults = map(query, input -> {
        return pagedPhotoRepository.searchPhotos(input);
    });

    private final LiveData<PagedList<Photo>> currentPhotoPagedList = switchMap(searchResults, input -> {
        return input.getPagedList();
    });

    private final LiveData<NetworkState> networkState = switchMap(searchResults, input -> input.getNetworkState());

    @Inject
    SearchViewModel(PagedPhotoRepository pagedPhotoRepository) {
        this.pagedPhotoRepository = pagedPhotoRepository;
    }

    boolean showNewSearch(String newQuery) {
        String trimmed = newQuery.trim();

        if (isQueryValid(trimmed)) {
            query.setValue(trimmed);
            return true;
        }

        return false;
    }

    private boolean isQueryValid(String newQuery) {
        if (newQuery.isEmpty()) {
            return false;
        }

        if (networkState.getValue() == null || networkState.getValue().getStatus() != NetworkState.Status.FAILED) {
            if (query.getValue() != null && query.getValue().equals(newQuery)) {
                return false;
            }
        }

        return true;
    }

    String currentQuery() {
        return query.getValue();
    }

    LiveData<PagedList<Photo>> getCurrentPhotoPagedList() {
        return currentPhotoPagedList;
    }

    LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

}
