package com.krueger.flickrfindr.repository.flickrapi.moshiadapters;

class FlickrPhotoSourceUrlGenerator {

    private final String URL_PREFIX = "https://farm";
    private final String URL_BODY = ".staticflickr.com/";
    private final String URL_SUFFIX = ".jpg";

    private final String THUMBNAIL_CODE = "q";
    private final String LARGE_CODE = "b";

    String generateThumbnailUrl(FlickrPhotoHolder photo) {
        return createUrl(photo, THUMBNAIL_CODE);
    }

    String generateLargeUrl(FlickrPhotoHolder photo) {
        return createUrl(photo, LARGE_CODE);
    }

    // Sample url: https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
    private String createUrl(FlickrPhotoHolder photo, String size) {
        return URL_PREFIX + photo.farmId
                + URL_BODY
                + photo.serverId + "/"
                + photo.id + "_"
                + photo.secret + "_"
                + size
                + URL_SUFFIX;
    }
}
