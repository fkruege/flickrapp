package com.krueger.flickrfindr.repository.flickrapi;

import com.krueger.flickrfindr.models.PhotoResults;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrRestApi {
    String API_KEY = "1508443e49213ff84d566777dc211f2a";

    // https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=b1904dce264fcab5ca08951ea7987c4f&text=Bears&per_page=25&page=1&format=json&nojsoncallback=1&api_sig=85941cce331d2f99b31a32ee5870b3c1
    @GET("?method=flickr.photos.search&per_page=25&format=json&nojsoncallback=1&safe_search=1&is_getty=1&api_key=" + API_KEY)
    Single<PhotoResults> searchPhotosFor(@Query("text") String text, @Query("page") int pageNo);

}