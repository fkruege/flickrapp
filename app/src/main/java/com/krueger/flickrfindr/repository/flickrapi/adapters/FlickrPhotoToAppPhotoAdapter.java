package com.krueger.flickrfindr.repository.flickrapi.adapters;

import com.krueger.flickrfindr.models.Photo;
import com.krueger.flickrfindr.models.PhotoResults;
import com.krueger.flickrfindr.repository.flickrapi.models.FlickrPhoto;
import com.krueger.flickrfindr.repository.flickrapi.models.FlickrPhotoPagingMetaData;
import com.krueger.flickrfindr.repository.flickrapi.models.FlickrPhotoSearchResults;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.krueger.flickrfindr.utils.StringUtils.getSafeInt;
import static com.krueger.flickrfindr.utils.StringUtils.getSafeString;


public class FlickrPhotoToAppPhotoAdapter {

    @FromJson
    PhotoResults photoResultsFromJson(FlickrPhotoSearchResults results) {

        List<Photo> photoList = new ArrayList<>();

        if (results.flickrPhotoPagingMetaData != null && results.flickrPhotoPagingMetaData.flickrPhotoList != null) {
            for (FlickrPhoto flickrPhoto : results.flickrPhotoPagingMetaData.flickrPhotoList) {
                Photo photo = mapPhotoFrom(flickrPhoto);
                photoList.add(photo);
            }
        }

        return mapPhotoResultsFrom(photoList, results.flickrPhotoPagingMetaData);
    }

    private Photo mapPhotoFrom(FlickrPhoto flickrPhoto) {
        String id = getSafeString(flickrPhoto.id);
        String title = getSafeString(flickrPhoto.title);
        String farmId = getSafeString(flickrPhoto.farm);
        String serverId = getSafeString(flickrPhoto.server);
        String secret = getSafeString(flickrPhoto.secret);

        FlickrPhotoHolder tempHolder = new FlickrPhotoHolder();
        tempHolder.id = id;
        tempHolder.title = title;
        tempHolder.farmId = farmId;
        tempHolder.serverId = serverId;
        tempHolder.secret = secret;

        FlickrPhotoSourceUrlGenerator urlGenerator = new FlickrPhotoSourceUrlGenerator();
        String thumbnailUrl = urlGenerator.generateThumbnailUrl(tempHolder);
        String largerPhotoUrl = urlGenerator.generateLargeUrl(tempHolder);

        return Photo.builder()
                .setId(id)
                .setTitle(title)
                .setThumbnailUrl(thumbnailUrl)
                .setLargePhotoUrl(largerPhotoUrl)
                .build();
    }

    private PhotoResults mapPhotoResultsFrom(List<Photo> photoList, FlickrPhotoPagingMetaData metadata) {
        int pageNo = 0;
        int totalPages = 0;
        int totalPhotos = 0;

        if (metadata != null) {
            pageNo = getSafeInt(metadata.page);
            totalPages = getSafeInt(metadata.pages);
            totalPhotos = getSafeInt(metadata.total);
        }

        return PhotoResults.builder()
                .setPhotoList(Collections.unmodifiableList(photoList))
                .setPageNo(pageNo)
                .setTotalPages(totalPages)
                .setTotalPhotos(totalPhotos)
                .build();

    }

    @ToJson
    String toJson(PhotoResults model) {
        return "";
    }


}
