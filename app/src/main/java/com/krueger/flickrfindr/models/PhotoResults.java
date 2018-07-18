package com.krueger.flickrfindr.models;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class PhotoResults {

    public static PhotoResults.Builder builder() {
        return new AutoValue_PhotoResults.Builder();
    }

    public abstract List<Photo> photoList();

    public abstract int pageNo();

    public abstract int totalPages();

    public abstract int totalPhotos();


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setPhotoList(List<Photo> value);

        public abstract Builder setPageNo(int value);

        public abstract Builder setTotalPages(int value);

        public abstract Builder setTotalPhotos(int value);

        public abstract PhotoResults build();
    }
}
