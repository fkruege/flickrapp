package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.krueger.flickrfindr.models.Photo;
import com.krueger.flickrfindr.models.PhotoResults;
import com.krueger.flickrfindr.repository.PhotoRepository;
import com.krueger.flickrfindr.utils.NetworkState;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

// https://proandroiddev.com/8-steps-to-implement-paging-library-in-android-d02500f7fffe
// https://stackoverflow.com/questions/50759456/paging-library-data-source-with-specific-page-index/50759663
public class PhotoDataSource extends PageKeyedDataSource<Integer, Photo> {

    private static final String TAG = PhotoDataSource.class.getSimpleName();


    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;
    private PhotoRepository photoRepository;
    private String query;

    public PhotoDataSource(PhotoRepository photoRepository, String query) {
        this.photoRepository = photoRepository;
        this.query = query;

        networkState = new MutableLiveData<>();
        initialLoading = new MutableLiveData<>();
    }


    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, Photo> callback) {

        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        photoRepository.searchPhotosFor(query, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PhotoResults>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(PhotoResults photoResults) {
                        callback.onResult(photoResults.photoList(), 1, 2);
                        initialLoading.postValue(NetworkState.LOADED);
                        networkState.postValue(NetworkState.LOADED);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage = e == null ? "unknown error" : e.getMessage();
                        networkState.setValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));

                    }
                });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Photo> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {

        networkState.postValue(NetworkState.LOADING);


        photoRepository.searchPhotosFor(query, params.key + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PhotoResults>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(PhotoResults photoResults) {
                        callback.onResult(photoResults.photoList(), params.key + 1);
                        networkState.setValue(NetworkState.LOADED);

                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage = e == null ? "unknown error" : e.getMessage();
                        networkState.setValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });

    }

//    @Override
//    public void loadBefore(@NonNull LoadParams<Long> params,
//                           @NonNull LoadCallback<Long, Article> callback) {
//
//    }
//
//    @Override
//    public void loadAfter(@NonNull LoadParams<Long> params,
//                          @NonNull LoadCallback<Long, Article> callback) {
//
//        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);
//
//        networkState.postValue(NetworkState.LOADING);
//
//        appController.getRestApi().fetchFeed(QUERY, API_KEY, params.key, params.requestedLoadSize).enqueue(new Callback<Feed>() {
//            @Override
//            public void onResponse(Call<Feed> call, Response<Feed> response) {
//                if (response.isSuccessful()) {
//                    long nextKey = (params.key == response.body().getTotalResults()) ? null : params.key + 1;
//                    callback.onResult(response.body().getArticles(), nextKey);
//                    networkState.postValue(NetworkState.LOADED);
//
//                } else
//                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
//            }
//
//            @Override
//            public void onFailure(Call<Feed> call, Throwable t) {
//                String errorMessage = t == null ? "unknown error" : t.getMessage();
//                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
//            }
//        });
//    }
}
