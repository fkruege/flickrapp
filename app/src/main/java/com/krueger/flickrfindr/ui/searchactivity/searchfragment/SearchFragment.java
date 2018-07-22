package com.krueger.flickrfindr.ui.searchactivity.searchfragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.krueger.flickrfindr.R;
import com.krueger.flickrfindr.app.injection.viewmodel.ViewModelFactory;
import com.krueger.flickrfindr.models.Photo;
import com.krueger.flickrfindr.ui.searchactivity.SearchViewModel;
import com.krueger.flickrfindr.ui.searchactivity.displayphotofragment.DisplayPhotoFragment;
import com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter.PhotoClickListener;
import com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter.PhotoResultsAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

public class SearchFragment extends Fragment implements PhotoClickListener {

    private static final int SPAN_COUNT = 2;

    @BindView(R.id.rvPhotos)
    RecyclerView rvPhotos;

    @Inject
    ViewModelFactory viewModelFactory;

    private Unbinder unbinder;

    SearchViewModel searchViewModel;

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
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        setupPhotoRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void photoClicked(Photo photo) {
        rvPhotos.requestFocus();
        DisplayPhotoFragment fragment = DisplayPhotoFragment.newInstance(photo.largePhotoUrl());
        fragment.show(getChildFragmentManager(), fragment.getClass().getSimpleName());
    }

    private void loadViewModel() {
        searchViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(SearchViewModel.class);
    }

    private void setupPhotoRecyclerView() {
        photoResultsAdapter = new PhotoResultsAdapter(this, Glide.with(this));
        rvPhotos.setAdapter(photoResultsAdapter);
        rvPhotos.setLayoutManager(getLayoutManager());

        // Order matters for this.
        // The search query change needs to be observed first before observing paged results.
        // If not the recycler view will not properly restore state on configuration change
        observeSearchQueryChanges();
        observeNewPageResults();

        observeNetworkChanges();
    }

    private void observeSearchQueryChanges() {
        searchViewModel.getQuery().observe(this, query -> photoResultsAdapter.submitList(null));
    }

    private void observeNewPageResults() {
        searchViewModel.getCurrentPhotoPagedList().observe(this, photos -> photoResultsAdapter.submitList(photos));
    }

    private void observeNetworkChanges() {
        searchViewModel.getNetworkState().observe(this, networkState -> photoResultsAdapter.setNetworkState(networkState));
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        if (this.getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return new GridLayoutManager(getContext(), SPAN_COUNT);
        } else {
            return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
    }
}
