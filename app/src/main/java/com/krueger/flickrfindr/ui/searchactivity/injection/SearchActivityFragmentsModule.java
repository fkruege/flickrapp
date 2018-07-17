package com.krueger.flickrfindr.ui.searchactivity.injection;


import com.krueger.flickrfindr.ui.searchactivity.searchfragment.SearchFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class SearchActivityFragmentsModule {
    @ContributesAndroidInjector
    abstract SearchFragment contributesSearchFragment();

}