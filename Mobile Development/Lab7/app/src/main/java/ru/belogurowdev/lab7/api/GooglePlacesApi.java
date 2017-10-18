package ru.belogurowdev.lab7.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.belogurowdev.lab7.model.PlacesList;

/**
 * Created by alexbelogurow on 18.10.2017.
 */

public interface GooglePlacesApi {

    @GET("/maps/api/place/textsearch/json")
    Observable<PlacesList> getPlacesBySearch(
            @Query("query") String query,
            @Query("key") String key,
            @Query("language") String lang);
}
