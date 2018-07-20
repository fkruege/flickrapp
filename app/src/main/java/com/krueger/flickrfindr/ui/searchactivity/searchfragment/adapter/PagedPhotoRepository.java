package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.krueger.flickrfindr.models.Photo;
import com.krueger.flickrfindr.repository.PhotoRepository;
import com.krueger.flickrfindr.ui.pagingsupport.Listing;
import com.krueger.flickrfindr.utils.Common;
import com.krueger.flickrfindr.utils.NetworkState;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import static android.arch.lifecycle.Transformations.switchMap;

public class PagedPhotoRepository {

    private final PhotoRepository photoRepository;
    private final Executor networkExecutor;

    @Inject
    PagedPhotoRepository(PhotoRepository photoRepository, Executor networkExecutor) {
        this.photoRepository = photoRepository;
        this.networkExecutor = networkExecutor;
    }

    public Listing<Photo> searchPhotos(String query) {
        PhotoDataSourceFactory photoDataSourceFactory = new PhotoDataSourceFactory(photoRepository, query);

        LiveData<PagedList<Photo>> pagedListLiveData = new LivePagedListBuilder<>(photoDataSourceFactory, Common.PAGE_SIZE)
                .setFetchExecutor(networkExecutor)
                .build();

        LiveData<NetworkState> networkState = switchMap(photoDataSourceFactory.getPhotoDataSourceLiveData(), PhotoDataSource::getNetworkState);


        return new Listing<>(pagedListLiveData, networkState);
    }

}
