package com.krueger.flickrfindr.models;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class PhotoResults {

    public static PhotoResults.Builder builder() {
        return new AutoValue_PhotoResults.Builder();
    }

    public abstract List<Photo> photoList();


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setPhotoList(List<Photo> value);

        public abstract PhotoResults build();
    }
}
