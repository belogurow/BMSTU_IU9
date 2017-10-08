package ru.belogurowdev.lab6.util;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.belogurowdev.lab6.api.WeatherApi;

/**
 * Created by alexbelogurow on 08.10.2017.
 */

public class App extends Application {

    private static final String WEATHER_BASE_URL = "http://api.openweathermap.org";

    public static WeatherApi mWeatherApi;

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mWeatherApi = retrofit.create(WeatherApi.class);
    }
}
