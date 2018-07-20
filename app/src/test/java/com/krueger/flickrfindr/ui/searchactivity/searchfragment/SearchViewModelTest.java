package com.krueger.flickrfindr.ui.searchactivity.searchfragment;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;

import com.krueger.flickrfindr.models.Photo;
import com.krueger.flickrfindr.ui.pagingsupport.Listing;
import com.krueger.flickrfindr.ui.searchactivity.searchfragment.adapter.PagedPhotoRepository;
import com.krueger.flickrfindr.utils.NetworkState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchViewModelTest {

    @Mock
    private PagedPhotoRepository mockRepository;

    @Mock
    private PagedList<Photo> mockPagedList;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @InjectMocks
    private SearchViewModel viewModel;

    private String newSearch = "dogs";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }


    @Test
    public void showNewSearchValid() {
        boolean result = viewModel.showNewSearch(newSearch);
        assertTrue(result);
    }

    @Test
    public void showNewSearchEmpty() {
        String emptySearch = "  ";

        boolean result = viewModel.showNewSearch(emptySearch);
        assertFalse(result);
    }


    @Test
    public void showNewSearchSame() {
        boolean result1 = viewModel.showNewSearch(newSearch);
        boolean result2 = viewModel.showNewSearch(newSearch);

        assertTrue(result1);
        assertFalse(result2);
    }


    @Test
    public void showNewSearchRepeateSearchWhenNetworkFailed() {
        startObserving();
        setupFailedSearch(newSearch);

        // simulate searching but network failure
        boolean result1 = viewModel.showNewSearch(newSearch);

        // search for the same thing again
        boolean result2 = viewModel.showNewSearch(newSearch);

        assertTrue(result2);
    }

    @Test
    public void getCurrentPhotoPagedList() {
        setupSuccessfulSearch(newSearch);

        List<PagedList<Photo>> expectedList = new ArrayList<>();

        viewModel.getCurrentPhotoPagedList().observeForever(photos -> expectedList.add(photos));

        viewModel.showNewSearch(newSearch);
        assertEquals(1, expectedList.size());

    }

    private void startObserving() {
        // In order to trigger the LiveData events it needs an observer.
        // If there is nothing observing the LiveData then nothing happens
        viewModel.getNetworkState().observeForever(networkState -> System.out.println("observing"));
    }


    private void setupFailedSearch(String newSearch) {
        MutableLiveData<PagedList<Photo>> liveDataPagedList = new MutableLiveData<>();
        liveDataPagedList.postValue(mockPagedList);

        MutableLiveData<NetworkState> liveDataNetworkState = new MutableLiveData<>();
        liveDataNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, "error"));

        Listing<Photo> newListing = new Listing<>(liveDataPagedList, liveDataNetworkState);

        when(mockRepository.searchPhotos(eq(newSearch))).thenReturn(newListing);
    }

    private void setupSuccessfulSearch(String newSearch) {
        MutableLiveData<PagedList<Photo>> liveDataPagedList = new MutableLiveData<>();
        liveDataPagedList.postValue(mockPagedList);

        MutableLiveData<NetworkState> liveDataNetworkState = new MutableLiveData<>();
        liveDataNetworkState.postValue(NetworkState.LOADED);

        Listing<Photo> newListing = new Listing<>(liveDataPagedList, liveDataNetworkState);

        when(mockRepository.searchPhotos(eq(newSearch))).thenReturn(newListing);
    }
}