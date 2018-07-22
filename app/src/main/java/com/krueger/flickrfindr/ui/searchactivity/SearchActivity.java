package com.krueger.flickrfindr.ui.searchactivity;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.krueger.flickrfindr.R;
import com.krueger.flickrfindr.app.injection.viewmodel.ViewModelFactory;
import com.krueger.flickrfindr.ui.searchactivity.searchfragment.SearchFragment;
import com.krueger.flickrfindr.ui.searchactivity.searchsuggestions.FlickrSearchSuggestions;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class SearchActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    protected DispatchingAndroidInjector<Fragment> androidInjector;

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    FlickrSearchSuggestions suggestions;

    SearchViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        loadViewModel();

        setContentView(R.layout.search_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SearchFragment.newInstance())
                    .commitNow();
        }

        restoreSearchQuery(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setupSearchMenu(menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (searchViewModel.getCurrentQuery() != null) {
            outState.putString(SearchManager.QUERY, searchViewModel.getCurrentQuery());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return androidInjector;
    }


    private void restoreSearchQuery(Bundle bundle) {
        if (bundle != null && bundle.containsKey(SearchManager.QUERY)) {
            String query = bundle.getString(SearchManager.QUERY);
            searchViewModel.showNewSearch(query);
        }
    }

    private void setupSearchMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchMenu).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchViewModel.getQuery().observe(this, query -> {
            if (!searchView.getQuery().toString().equals(query)) {
                restoreSearchViewQuery(query, menu, searchView);
            }
        });
    }

    private void restoreSearchViewQuery(String query, Menu menu, SearchView searchView) {
        MenuItem searchMenuItem = menu.findItem(R.id.searchMenu);
        searchMenuItem.expandActionView();

        searchView.onActionViewExpanded();
        searchView.setQuery(query, false);
        searchView.clearFocus();
    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            suggestions.saveRecentQuery(query, null);

            searchViewModel.showNewSearch(query);
        }
    }

    private void loadViewModel() {
        searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);
    }
}
