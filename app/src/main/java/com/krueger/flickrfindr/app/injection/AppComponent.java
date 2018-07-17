package com.krueger.flickrfindr.app.injection;

import com.krueger.flickrfindr.app.FlickrApp;
import com.krueger.flickrfindr.repository.flickrapi.injection.FlickrNetworkingModule;
import com.krueger.flickrfindr.ui.searchactivity.injection.SearchActivityModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules =
        {
                AndroidInjectionModule.class
                , AppModule.class
                , FlickrNetworkingModule.class
                , SearchActivityModule.class
        }
)
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(FlickrApp flickrApp);

        AppComponent build();
    }

    void inject(FlickrApp app);

}
