package Utils;


import com.karine.go4lunch.BuildConfig;

import io.reactivex.Observable;
import models.NearbySearchAPI.GoogleApi;
import models.PlaceDetailsAPI.PlaceDetail;
import retrofit2.http.GET;
import retrofit2.http.Query;




public interface Go4LunchService {

    String API_KEY = "AIzaSyDRx8LRA_qMswRC9G8DdyY9P-pe-ImYvwg";


    //GoogleAPI API Request
    @GET("maps/api/place/nearbysearch/json?key="+API_KEY)
    Observable<GoogleApi> getRestaurants(@Query("location") String location, @Query("radius") int radius, @Query("type") String type);

    //PlaceDetails API Request
    @GET("maps/api/place/details/json?key="+API_KEY)
    Observable<PlaceDetail> getDetails(@Query("fields") String fields, @Query("place_id") String placeId);
}
