package com.karine.go4lunch.Utils;


import com.google.android.libraries.places.widget.Autocomplete;
import com.karine.go4lunch.BuildConfig;

import io.reactivex.Observable;

import com.karine.go4lunch.models.AutocompleteAPI.AutocompleteResult;
import com.karine.go4lunch.models.NearbySearchAPI.GoogleApi;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;
import retrofit2.http.GET;
import retrofit2.http.Query;




public interface Go4LunchService {

      String GOOGLE_MAP_API_KEY = BuildConfig.GOOGLE_MAP_API_KEY;

    //GoogleMap API Request
    @GET("maps/api/place/nearbysearch/json?key="+GOOGLE_MAP_API_KEY)
    Observable<GoogleApi> getRestaurants(@Query("location") String location, @Query("radius") int radius, @Query("type") String type);

    //PlaceDetails API Request
    @GET("maps/api/place/details/json?key="+GOOGLE_MAP_API_KEY)
    Observable<PlaceDetail> getDetails(@Query("place_id") String placeId);

    //Autocomplete API Request
    @GET("maps/api/place/autocomplete/json?key="+GOOGLE_MAP_API_KEY)
    Observable<AutocompleteResult> getAutocomplete(@Query("input") String input, @Query("radius") int radius, @Query("location") String location);
}


