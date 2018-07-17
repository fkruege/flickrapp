
package com.krueger.flickrfindr.repository.flickrapi.models;

import com.squareup.moshi.Json;

public class FlickrPhotoSearchResults {

    @Json(name = "photos")
    public FlickrPhotoPagingMetaData flickrPhotoPagingMetaData;
    @Json(name = "stat")
    public String stat;

}
