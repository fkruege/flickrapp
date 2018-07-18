package com.krueger.flickrfindr.ui.searchactivity.searchfragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.krueger.flickrfindr.models.Photo;
import com.krueger.flickrfindr.models.PhotoResults;
import com.krueger.flickrfindr.repository.PhotoRepository;
import com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter.PhotoDataSourceFactory;
import com.krueger.flickrfindr.utils.NetworkState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

import static com.krueger.flickrfindr.utils.Common.PAGE_SIZE;

// For paging
// https://proandroiddev.com/8-steps-to-implement-paging-library-in-android-d02500f7fffe
// https://codelabs.developers.google.com/codelabs/android-paging/index.html#8

public class SearchViewModel extends ViewModel {

    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Photo>> pagedPhotoListLiveData;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private PhotoRepository photoRepository;


    @Inject
    SearchViewModel(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

//    LiveData<PhotoResults> photoResultsLiveData() {
//        return photoResults;
//    }


    void search(String query) {

        PhotoDataSourceFactory photoDataSourceFactory = new PhotoDataSourceFactory(photoRepository, query);

        networkState = Transformations.switchMap(photoDataSourceFactory.getMutableLiveData(), dataSource -> dataSource.getNetworkState());

        PagedList.Config pagedListConfig
                = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .setPageSize(PAGE_SIZE)
                .build();

        pagedPhotoListLiveData = (new LivePagedListBuilder(photoDataSourceFactory, pagedListConfig))
                .build();

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Photo>> getPagedPhotoListLiveData() {
        return pagedPhotoListLiveData;
    }
}
