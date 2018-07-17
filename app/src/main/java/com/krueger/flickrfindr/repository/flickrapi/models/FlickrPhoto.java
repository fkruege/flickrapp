
package com.krueger.flickrfindr.repository.flickrapi.models;

import com.squareup.moshi.Json;

public class FlickrPhoto {

    @Json(name = "id")
    public String id;
    @Json(name = "owner")
    public String owner;
    @Json(name = "secret")
    public String secret;
    @Json(name = "server")
    public String server;
    @Json(name = "farm")
    public Integer farm;
    @Json(name = "title")
    public String title;
    @Json(name = "ispublic")
    public Integer ispublic;
    @Json(name = "isfriend")
    public Integer isfriend;
    @Json(name = "isfamily")
    public Integer isfamily;

}
