package com.krueger.flickrfindr.ui.searchactivity.searchsuggestions;

import android.content.SearchRecentSuggestionsProvider;

public class FlickrSearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.krueger.flickrfindr.ui.searchactivity.searchsuggestions.FlickrSearchSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public FlickrSearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
