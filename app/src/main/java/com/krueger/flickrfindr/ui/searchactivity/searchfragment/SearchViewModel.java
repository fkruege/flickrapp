package com.krueger.flickrfindr.ui.searchactivity.searchfragment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.krueger.flickrfindr.models.PhotoResults;
import com.krueger.flickrfindr.repository.PhotoRepository;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

// For paging
// https://proandroiddev.com/8-steps-to-implement-paging-library-in-android-d02500f7fffe
// https://codelabs.developers.google.com/codelabs/android-paging/index.html#8

public class SearchViewModel extends ViewModel {

    private MutableLiveData<PhotoResults> photoResults;

    private PhotoRepository photoRepository;


    @Inject
    public SearchViewModel(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }


    void search(String text) {
        photoRepository.searchPhotosFor(text, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PhotoResults>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(PhotoResults results) {
                        Timber.d("Success: " + results.photoList().size());
                        photoResults.setValue(results);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error retrieving photos.");
                    }
                });

    }

    public void testMe() {
        Timber.d("In SearchViewModel");
    }

}
