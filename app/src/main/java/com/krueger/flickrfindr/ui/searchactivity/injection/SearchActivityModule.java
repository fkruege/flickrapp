package com.krueger.flickrfindr.ui.searchactivity.injection;

import com.krueger.flickrfindr.ui.searchactivity.SearchActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SearchActivityModule {
    @ContributesAndroidInjector(modules = { SearchActivityFragmentsModule.class })

    abstract SearchActivity contributesActivity();

}