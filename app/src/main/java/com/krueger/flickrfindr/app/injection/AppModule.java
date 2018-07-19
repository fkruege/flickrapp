package com.krueger.flickrfindr.app.injection;

import android.content.Context;

import com.krueger.flickrfindr.app.FlickrApp;
import com.krueger.flickrfindr.app.injection.viewmodel.ViewModelModule;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ViewModelModule.class})
public class AppModule {

    @Provides
    Context provideContext(FlickrApp app) {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    Executor provideNetworkExecutor() {
        return Executors.newFixedThreadPool(5);
    }
}
