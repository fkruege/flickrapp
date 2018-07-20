package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.krueger.flickrfindr.models.Photo;
import com.krueger.flickrfindr.models.PhotoResults;
import com.krueger.flickrfindr.repository.PhotoRepository;
import com.krueger.flickrfindr.utils.NetworkState;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

// https://github.com/googlesamples/android-architecture-components/tree/master/PagingWithNetworkSample
// https://proandroiddev.com/8-steps-to-implement-paging-library-in-android-d02500f7fffe
// https://stackoverflow.com/questions/50759456/paging-library-data-source-with-specific-page-index/50759663
class PhotoDataSource extends PageKeyedDataSource<Integer, Photo> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();

    private PhotoRepository photoRepository;
    private String query;

    PhotoDataSource(PhotoRepository photoRepository, String query) {
        this.photoRepository = photoRepository;
        this.query = query;
    }


    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Photo> callback) {
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, Photo> callback) {

        networkState.postValue(NetworkState.LOADING);

        photoRepository.searchPhotos(query, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PhotoResults>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(PhotoResults photoResults) {
                        networkState.postValue(NetworkState.LOADED);
                        callback.onResult(photoResults.photoList(), 1, 2);
                    }

                    @Override
                    public void onError(Throwable e) {
                        postNetworkStateError(e);
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {

        networkState.postValue(NetworkState.LOADING);

        photoRepository.searchPhotos(query, params.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PhotoResults>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(PhotoResults photoResults) {
                        callback.onResult(photoResults.photoList(), params.key + 1);
                        networkState.postValue(NetworkState.LOADED);
                    }

                    @Override
                    public void onError(Throwable e) {
                        postNetworkStateError(e);
                    }
                });

    }


    @Override
    public void invalidate() {
        super.invalidate();
        cleanUp();
    }

    LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    private void postNetworkStateError(Throwable e) {
        String errorMessage = e == null ? "unknown error" : e.getMessage();
        NetworkState status = new NetworkState(NetworkState.Status.FAILED, errorMessage);
        networkState.postValue(status);
    }


    private void cleanUp() {
        compositeDisposable.clear();
    }


}
