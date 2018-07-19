package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.krueger.flickrfindr.models.Photo;
import com.krueger.flickrfindr.models.PhotoResults;
import com.krueger.flickrfindr.repository.PhotoRepository;
import com.krueger.flickrfindr.utils.NetworkState;

import java.util.concurrent.Executor;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

// https://github.com/googlesamples/android-architecture-components/tree/master/PagingWithNetworkSample
// https://proandroiddev.com/8-steps-to-implement-paging-library-in-android-d02500f7fffe
// https://stackoverflow.com/questions/50759456/paging-library-data-source-with-specific-page-index/50759663
public class PhotoDataSource extends PageKeyedDataSource<Integer, Photo> {

    private static final String TAG = PhotoDataSource.class.getSimpleName();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();

    private MutableLiveData<NetworkState> initialLoading = new MutableLiveData<>();

    private Action retryAction = null;

    private PhotoRepository photoRepository;
    private String query;
    private Executor retryExecutor;

    PhotoDataSource(PhotoRepository photoRepository, String query, Executor retryExecutor) {
        this.photoRepository = photoRepository;
        this.query = query;
        this.retryExecutor = retryExecutor;
    }


    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Photo> callback) {

    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, Photo> callback) {

        networkState.postValue(NetworkState.LOADING);
        initialLoading.postValue(NetworkState.LOADING);

        photoRepository.searchPhotosFor(query, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PhotoResults>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(PhotoResults photoResults) {
                        retryAction = null;
                        networkState.postValue(NetworkState.LOADED);
                        initialLoading.postValue(NetworkState.LOADED);
                        callback.onResult(photoResults.photoList(), 1, 2);
                    }

                    @Override
                    public void onError(Throwable e) {
                        retryAction = () -> loadInitial(params, callback);

                        String errorMessage = e == null ? "unknown error" : e.getMessage();
                        NetworkState status = new NetworkState(NetworkState.Status.FAILED, errorMessage);

                        networkState.setValue(status);
                        initialLoading.setValue(status);
                    }
                });

    }


    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Photo> callback) {

        networkState.postValue(NetworkState.LOADING);

        photoRepository.searchPhotosFor(query, params.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PhotoResults>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(PhotoResults photoResults) {
                        retryAction = null;
                        callback.onResult(photoResults.photoList(), params.key + 1);
                        networkState.postValue(NetworkState.LOADED);
                    }

                    @Override
                    public void onError(Throwable e) {
                        retryAction = () -> loadAfter(params, callback);
                        String errorMessage = e == null ? "unknown error" : e.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });

    }


    @Override
    public void invalidate() {
        super.invalidate();
        cleanUp();
    }

    void cleanUp() {
        compositeDisposable.clear();
    }

    void retryAllFailed() {
        Action previousRetry = retryAction;
        retryAction = null;

        if (previousRetry != null) {
            retryExecutor.execute(() -> {
                try {
                    previousRetry.run();
                } catch (Exception e) {
                    Timber.e(e);
                }
            });
        }
    }

    MutableLiveData getNetworkState() {
        return networkState;
    }

    MutableLiveData getInitialLoading() {
        return initialLoading;
    }

}
