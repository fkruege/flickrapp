package com.krueger.flickrfindr.app;

import javax.inject.Inject;

import timber.log.Timber;

public class TestDepnendency {

    private FlickrApp flickrApp;

    @Inject
    public TestDepnendency(FlickrApp flickrApp) {
        this.flickrApp = flickrApp;
    }

    public void testMe() {
        Timber.d(flickrApp.getPackageName());
    }

}
