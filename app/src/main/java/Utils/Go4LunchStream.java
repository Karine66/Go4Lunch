package Utils;



import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import models.NearbySearchAPI.GoogleApi;
import models.NearbySearchAPI.ResultSearch;
import models.PlaceDetailsAPI.PlaceDetail;
import models.PlaceDetailsAPI.Result;
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

    public static Observable<PlaceDetail> streamFetchRestaurantDetails(String location, int radius, String type) {
        return streamFetchRestaurants(location, radius, type)
                .map(new Function<GoogleApi, List<ResultSearch>>() {
                    @Override
                    public List<ResultSearch> apply(GoogleApi googleApi) throws Exception {
                        return googleApi.getResults();
                    }
                })
                .flatMap(new Function<List<ResultSearch>, Observable<PlaceDetail>>() {


                    private int resultDetail;


                    @Override
                    public Observable<PlaceDetail> apply(List<ResultSearch> resultSearch) throws Exception {

                        for (ResultSearch resultDetail : resultSearch) {
                            resultDetail.getName();

                            Log.d("TestFor", resultDetail.getName());

                            // return streamFetchDetails(fields, resultSearch.get(0).getPlaceId());
                        }
                        return streamFetchDetails(fields, resultSearch.get(resultDetail).getPlaceId());
                    }

                });
    }
}
