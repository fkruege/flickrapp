package com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.krueger.flickrfindr.models.Photo;
import com.krueger.flickrfindr.repository.PhotoRepository;

class PhotoDataSourceFactory extends DataSource.Factory<Integer, Photo> {

    private PhotoDataSource photoDataSource;
    private PhotoRepository photoRepository;
    private String query;

    private MutableLiveData<PhotoDataSource> mutableLiveData;

    PhotoDataSourceFactory(PhotoRepository photoRepository, String query) {
        this.photoRepository = photoRepository;
        this.query = query;
        this.mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, Photo> create() {
        photoDataSource = new PhotoDataSource(photoRepository, query);
        mutableLiveData.postValue(photoDataSource);
        return photoDataSource;
    }


    MutableLiveData<PhotoDataSource> getPhotoDataSourceLiveData() {
        return mutableLiveData;
    }
}
