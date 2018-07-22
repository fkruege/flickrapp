package com.krueger.flickrfindr.ui.searchactivity.searchsuggestions;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.SearchRecentSuggestions;

public class FlickrSearchSuggestions extends SearchRecentSuggestions {

    private static final int MAX_ENTRIES = 5;

    public FlickrSearchSuggestions(Context context, String authority, int mode) {
        super(context, authority, mode);
    }

    @Override
    protected void truncateHistory(ContentResolver cr, int maxEntries) {
        super.truncateHistory(cr, MAX_ENTRIES);
    }
}
