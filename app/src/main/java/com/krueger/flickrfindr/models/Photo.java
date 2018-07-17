package com.krueger.flickrfindr.models;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Photo {
    public static Builder builder() {
        return new AutoValue_Photo.Builder();
    }

    public abstract String id();

    public abstract String title();

    public abstract String thumbnailUrl();

    public abstract String largePhotoUrl();


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(String value);

        public abstract Builder setTitle(String value);

        public abstract Builder setThumbnailUrl(String value);

        public abstract Builder setLargePhotoUrl(String value);

        public abstract Photo build();
    }
}
