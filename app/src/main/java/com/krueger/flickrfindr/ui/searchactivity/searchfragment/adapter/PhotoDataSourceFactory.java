package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.krueger.flickrfindr.repository.PhotoRepository;

import javax.inject.Inject;

public class PhotoDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PhotoDataSource> mutableLiveData;
    private PhotoDataSource photoDataSource;
    private PhotoRepository photoRepository;
    private String query;

    public PhotoDataSourceFactory(PhotoRepository photoRepository, String query) {
        this.photoRepository = photoRepository;
        this.query = query;
        this.mutableLiveData = new MutableLiveData<PhotoDataSource>();
    }

    @Override
    public DataSource create() {
        photoDataSource = new PhotoDataSource(photoRepository, query);
        mutableLiveData.postValue(photoDataSource);
        return photoDataSource;
    }


    public MutableLiveData<PhotoDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
