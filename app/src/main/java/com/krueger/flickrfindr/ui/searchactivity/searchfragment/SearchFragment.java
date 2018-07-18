package com.krueger.flickrfindr.ui.searchactivity.searchfragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krueger.flickrfindr.R;
import com.krueger.flickrfindr.app.injection.viewmodel.ViewModelFactory;
import com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter.PhotoResultsAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

public class SearchFragment extends Fragment {

    // Suggestions: https://abhiandroid.com/ui/searchview
    @BindView(R.id.search)
    SearchView searchView;

    @BindView(R.id.rvPhotos)
    RecyclerView rvPhotos;

    @Inject
    ViewModelFactory viewModelFactory;

    private Unbinder unbinder;

    private SearchViewModel searchViewModel;

    private PhotoResultsAdapter photoResultsAdapter;


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);

        loadViewModel();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        setupSearchView();
        setupPhotoRecyclerView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void loadViewModel() {
        searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);
    }

    private void setupSearchView() {
        addListenersToSearchView();
    }

    private void addListenersToSearchView() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewModel.search(query);

                searchViewModel.getPagedPhotoListLiveData().observe(SearchFragment.this, photos -> photoResultsAdapter.submitList(photos));

                searchViewModel.getNetworkState().observe(SearchFragment.this, networkState -> {
                    photoResultsAdapter.setNetworkState(networkState);
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setupPhotoRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        photoResultsAdapter = new PhotoResultsAdapter(this);

        rvPhotos.setLayoutManager(linearLayoutManager);
        rvPhotos.setAdapter(photoResultsAdapter);

//
//                searchViewModel.photoResults.observe(this, new Observer<PhotoResults>() {
//                    @Override
//                    public void onChanged(@Nullable PhotoResults photoResults) {
//                        photoResultsAdapter.update(photoResults);
//                        photoResultsAdapter.notifyDataSetChanged();
//                    }
//                });


    }
}
