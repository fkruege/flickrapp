package com.krueger.flickrfindr.app;

import android.app.Activity;
import android.app.Application;

import com.krueger.flickrfindr.app.injection.AppComponent;
import com.krueger.flickrfindr.app.injection.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;


public class FlickrApp extends Application implements HasActivityInjector {

    @Inject
    protected DispatchingAndroidInjector<Activity> androidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.d("App onCreate");

        injectDagger();
        plantTimber();
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return androidInjector;
    }

    private void injectDagger() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
    }

    private void plantTimber() {
        Timber.plant(new Timber.DebugTree());
    }
}
