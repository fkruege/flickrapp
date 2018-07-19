package com.krueger.flickrfindr.repository.flickrapi.injection;

import android.content.Context;

import com.krueger.flickrfindr.repository.flickrapi.FlickrRestApi;
import com.krueger.flickrfindr.repository.flickrapi.moshiadapters.FlickrPhotoToAppPhotoAdapter;
import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class FlickrNetworkingModule {

    private final String FLICKR_URL = "https://api.flickr.com/services/rest/";

    @Provides
    Cache provideOkHttpCache(Context context) {
        long cacheSize = 10L * 1024L * 1024L;
        return new Cache(context.getCacheDir(), cacheSize);
    }

    @Provides
    OkHttpClient provideOkHttpClient(Cache cache) {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient
                .Builder()
                .cache(cache)
                .addNetworkInterceptor(logger)
                .build();

    }

    @Provides
    Moshi provideMoshi() {
        return new Moshi.Builder()
                .add(new FlickrPhotoToAppPhotoAdapter())
                .build();
    }

    @Provides
    Retrofit provideRetrofit(Moshi moshi, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(FLICKR_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    FlickrRestApi provideFlickrRestApi(Retrofit retrofit){
        return retrofit.create(FlickrRestApi.class);
    }


}
