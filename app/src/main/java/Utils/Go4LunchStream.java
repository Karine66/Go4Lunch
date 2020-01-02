package Utils;



import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import models.NearbySearchAPI.GoogleApi;
import models.PlaceDetailsAPI.PlaceDetail;
import retrofit2.http.Query;

public class Go4LunchStream {

    //Create stream google restaurants
    public static Observable<GoogleApi> streamFetchRestaurants (String location, int radius, String type) {
        Go4LunchService go4LunchService = Go4LunchRetrofitObject.retrofit.create(Go4LunchService.class);
        return go4LunchService.getRestaurants(location, radius, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<PlaceDetail> streamFetchDetails (@Query("fields") String fields, @Query("place_id") String placeId ) {
        Go4LunchService go4LunchService = Go4LunchRetrofitObject.retrofit.create(Go4LunchService.class);
        return  go4LunchService.getDetails(fields, placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
