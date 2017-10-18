package ru.belogurowdev.lab7.util;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.belogurowdev.lab7.api.GooglePlacesApi;
import ru.belogurowdev.lab7.model.PlacesList;

/**
 * Created by alexbelogurow on 18.10.2017.
 */

public class App extends Application {

    // https://maps.googleapis.com/maps/api/place/textsearch/json?query=bmstu&key=AIzaSyAuJIEnY4TcR-G67YJSgS2CNbPJNABzs3s

    private static final String BASE_URL = "https://maps.googleapis.com";
    public static GooglePlacesApi mGooglePlacesApi;

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mGooglePlacesApi = retrofit.create(GooglePlacesApi.class);
    }
}
