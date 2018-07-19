package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.krueger.flickrfindr.models.Photo;
import com.krueger.flickrfindr.repository.PhotoRepository;

import java.util.concurrent.Executor;

public class PhotoDataSourceFactory extends DataSource.Factory<Integer, Photo> {

    private PhotoDataSource photoDataSource;
    private PhotoRepository photoRepository;
    private String query;
    private Executor retryExecutor;

    private MutableLiveData<PhotoDataSource> mutableLiveData;

    public PhotoDataSourceFactory(PhotoRepository photoRepository, String query, Executor retryExecutor) {
        this.photoRepository = photoRepository;
        this.query = query;
        this.retryExecutor = retryExecutor;
        this.mutableLiveData = new MutableLiveData<PhotoDataSource>();
    }

    @Override
    public DataSource create() {
        photoDataSource = new PhotoDataSource(photoRepository, query, retryExecutor);
        mutableLiveData.postValue(photoDataSource);
        return photoDataSource;
    }



    public MutableLiveData<PhotoDataSource> getPhotoDataSourceLiveData() {
        return mutableLiveData;
    }
}
