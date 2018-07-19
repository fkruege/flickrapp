package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.arch.core.util.Function;
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

import io.reactivex.functions.Action;

import static android.arch.lifecycle.Transformations.switchMap;

public class PagedPhotoRepository {

    private final PhotoRepository photoRepository;
    private final Executor networkExecutor;

    @Inject
    public PagedPhotoRepository(PhotoRepository photoRepository, Executor networkExecutor) {
        this.photoRepository = photoRepository;
        this.networkExecutor = networkExecutor;
    }

    public Listing<Photo> searchPhotos(String query) {
        PhotoDataSourceFactory photoDataSourceFactory = new PhotoDataSourceFactory(photoRepository, query, networkExecutor);

        LiveData<PagedList<Photo>> pagedListLiveData = new LivePagedListBuilder<>(photoDataSourceFactory, Common.PAGE_SIZE)
                .setFetchExecutor(networkExecutor)
                .build();

        LiveData<NetworkState> refreshState = switchMap(photoDataSourceFactory.getPhotoDataSourceLiveData()
                , (Function<PhotoDataSource, LiveData<NetworkState>>) PhotoDataSource::getInitialLoading);

        LiveData<NetworkState> networkState = switchMap(photoDataSourceFactory.getPhotoDataSourceLiveData()
                , (Function<PhotoDataSource, LiveData<NetworkState>>) PhotoDataSource::getNetworkState);

        Action retryAction = createRetryAction(photoDataSourceFactory);

        Action refreshAction = createRefreshAction(photoDataSourceFactory);

        return new Listing<>(
                pagedListLiveData,
//                networkState,
                switchMap(photoDataSourceFactory.getPhotoDataSourceLiveData(), (Function<PhotoDataSource, LiveData<NetworkState>>) dataSource -> dataSource.getNetworkState()),

                refreshState,
                refreshAction,
                retryAction
        );
    }

    private Action createRetryAction(PhotoDataSourceFactory photoDataSourceFactory) {
        return () -> {
            PhotoDataSource photoDataSource = photoDataSourceFactory.getPhotoDataSourceLiveData().getValue();
            if (photoDataSource != null) {
                photoDataSource.retryAllFailed();
            }
        };
    }

    private Action createRefreshAction(PhotoDataSourceFactory photoDataSourceFactory) {
        return () -> {
            PhotoDataSource photoDataSource = photoDataSourceFactory.getPhotoDataSourceLiveData().getValue();
            if (photoDataSource != null) {
                photoDataSource.invalidate();
            }
        };
    }


}
