package Utils;


import io.reactivex.Observable;
import models.GoogleApi;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Go4LunchService {

    String API_KEY = "AIzaSyCM-y8wLzp1QBMtzH_iokAhyRgTutq016g";

    //GoogleAPI API Request
    @GET("maps/api/place/nearbysearch/json?key="+API_KEY)
    Observable<GoogleApi> getRestaurants(@Query("location") String location, @Query("radius") int radius, @Query("type") String type);
}
