package com.krueger.flickrfindr.ui.searchactivity.injection;

import com.krueger.flickrfindr.ui.searchactivity.SearchActivity;
import com.krueger.flickrfindr.ui.searchactivity.searchsuggestions.FlickrSearchSuggestionProvider;
import com.krueger.flickrfindr.ui.searchactivity.searchsuggestions.FlickrSearchSuggestions;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchActivityDependenciesModule {

    @Provides
    FlickrSearchSuggestions provideFlickrSearchSuggestions(SearchActivity activity) {
        FlickrSearchSuggestions suggestions = new FlickrSearchSuggestions(activity,
                FlickrSearchSuggestionProvider.AUTHORITY, FlickrSearchSuggestionProvider.MODE);

        return suggestions;
    }

}
