package ru.belogurowdev.lab7.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.belogurowdev.lab7.model.placeInfo.PlaceInfo;
import ru.belogurowdev.lab7.model.placesList.PlacesList;

/**
 * Created by alexbelogurow on 18.10.2017.
 */

public interface GooglePlacesApi {

    @GET("/maps/api/place/textsearch/json?opennow")
    Observable<PlacesList> getPlacesBySearchWithOpen(
            @Query("query") String query,
            @Query("key") String key,
            @Query("language") String lang);

    @GET("/maps/api/place/textsearch/json")
    Observable<PlacesList> getPlacesBySearch(
            @Query("query") String query,
            @Query("key") String key,
            @Query("language") String lang);

    @GET("/maps/api/place/details/json")
    Observable<PlaceInfo> getPlaceById(
            @Query("placeid") String placeId,
            @Query("key") String key,
            @Query("language") String language);

    // https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=AIzaSyAuJIEnY4TcR-G67YJSgS2CNbPJNABzs3s
    // /maps/api/place/details/json?placeid=e2b036638e205a14e0110bc2e5052a983055cee1&key=AIzaSyAuJIEnY4TcR-G67YJSgS2CNbPJNABzs3s
}
