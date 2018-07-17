
package com.krueger.flickrfindr.repository.flickrapi.models;

import java.util.List;
import com.squareup.moshi.Json;

public class FlickrPhotoPagingMetaData {

    @Json(name = "page")
    public Integer page;
    @Json(name = "pages")
    public String pages;
    @Json(name = "perpage")
    public Integer perpage;
    @Json(name = "total")
    public String total;
    @Json(name = "photo")
    public List<FlickrPhoto> flickrPhotoList = null;

}
