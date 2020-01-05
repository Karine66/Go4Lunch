package Utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import models.NearbySearchAPI.GoogleApi;
import models.NearbySearchAPI.ResultSearch;
import models.PlaceDetailsAPI.PlaceDetail;
import retrofit2.http.Query;

public class Go4LunchStream {

    public static String fields = "opening_hours,photo,rating,vicinity,name";

    //Create stream google restaurants
    public static Observable<GoogleApi> streamFetchRestaurants(String location, int radius, String type) {
        Go4LunchService go4LunchService = Go4LunchRetrofitObject.retrofit.create(Go4LunchService.class);
        return go4LunchService.getRestaurants(location, radius, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<PlaceDetail> streamFetchDetails(@Query("fields") String fields, @Query("place_id") String placeId) {
        Go4LunchService go4LunchService = Go4LunchRetrofitObject.retrofit.create(Go4LunchService.class);
        return go4LunchService.getDetails(fields, placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
    //For 2 chained requests
    public static Single<List<PlaceDetail>> streamFetchRestaurantDetails(String location, int radius, String type) {
        return streamFetchRestaurants(location, radius, type)
                .flatMapIterable(new Function<GoogleApi, List<ResultSearch>>() {
                    @Override
                    public List<ResultSearch> apply(GoogleApi googleApi) throws Exception {
                        return googleApi.getResults();
                    }
                })
                .flatMap(new Function<ResultSearch, Observable<PlaceDetail>>() {
                    @Override
                    public Observable<PlaceDetail> apply(ResultSearch resultSearch) throws Exception {
                        return streamFetchDetails(fields, resultSearch.getPlaceId());
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
