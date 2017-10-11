package ru.belogurowdev.lab7.api;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.belogurowdev.lab7.model.WeatherData;

/**
 * Created by alexbelogurow on 08.10.2017.
 */

public interface WeatherApi {

    @GET("data/2.5/forecast/daily")
    Observable<WeatherData> getWeatherByCity(
            @Query("q")     String city,
            @Query("cnt")   String dayCount,
            @Query("APPID") String key,
            @Query("units") String units);
}
