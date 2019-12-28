package Utils;


import io.reactivex.Observable;
import models.GoogleApi;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Go4LunchService {

    String API_KEY = "AIzaSyDRx8LRA_qMswRC9G8DdyY9P-pe-ImYvwg";

    
    //GoogleAPI API Request
    @GET("maps/api/place/nearbysearch/json?key="+API_KEY)
    Observable<GoogleApi> getRestaurants(@Query("location") String location, @Query("radius") int radius, @Query("type") String type);
}
