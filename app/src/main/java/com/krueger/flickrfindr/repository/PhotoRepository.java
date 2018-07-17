package com.krueger.flickrfindr.repository;

import com.krueger.flickrfindr.models.PhotoResults;
import com.krueger.flickrfindr.repository.flickrapi.FlickrRestApi;

import javax.inject.Inject;

import io.reactivex.Single;

public class PhotoRepository {

    private FlickrRestApi flickrRestApi;

    @Inject
    public PhotoRepository(FlickrRestApi flickrRestApi) {
        this.flickrRestApi = flickrRestApi;
    }

    public Single<PhotoResults> searchPhotosFor(String text, int pageNo) {
        return flickrRestApi.searchPhotosFor(text, pageNo);
    }
}
