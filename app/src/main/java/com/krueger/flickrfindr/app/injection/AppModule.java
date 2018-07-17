package com.krueger.flickrfindr.app.injection;

import android.content.Context;

import com.krueger.flickrfindr.app.FlickrApp;
import com.krueger.flickrfindr.app.injection.viewmodel.ViewModelModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ViewModelModule.class})
public class AppModule {

    @Provides
    Context provideContext(FlickrApp app) {
        return app.getApplicationContext();
    }
}
