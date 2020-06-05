package com.karine.go4lunch.utils;

import com.karine.go4lunch.models.AutocompleteAPI.AutocompleteResult;
import com.karine.go4lunch.models.AutocompleteAPI.Prediction;
import com.karine.go4lunch.models.NearbySearchAPI.GoogleApi;
import com.karine.go4lunch.models.NearbySearchAPI.ResultSearch;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class Go4LunchStream {


    //Create stream google restaurants
    public static Observable<GoogleApi> streamFetchRestaurants(String location, int radius, String type) {
        Go4LunchService go4LunchService = Go4LunchRetrofitObject.retrofit.create(Go4LunchService.class);
        return go4LunchService.getRestaurants(location, radius, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<PlaceDetail> streamFetchDetails(String placeId) {
        Go4LunchService go4LunchService = Go4LunchRetrofitObject.retrofit.create(Go4LunchService.class);
        return go4LunchService.getDetails(placeId)
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
                        return streamFetchDetails(resultSearch.getPlaceId());
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //For autocomplete
    public static Observable<AutocompleteResult> streamFetchAutocomplete(String input, int radius, String location, String type) {
        Go4LunchService go4LunchService = Go4LunchRetrofitObject.retrofit.create(Go4LunchService.class);
        return go4LunchService.getAutocomplete(input, radius, location, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    //For autocomplete 2 chained request
    public static Single<List<PlaceDetail>> streamFetchAutocompleteInfos(String input, int radius, String location, String type) {
        return streamFetchAutocomplete(input, radius, location, type)
                .flatMapIterable(new Function<AutocompleteResult, List<Prediction>>() {
                    List<Prediction> food = new ArrayList<>();

                    @Override
                    public List<Prediction> apply(AutocompleteResult autocompleteResult) throws Exception {

                        for (Prediction prediction : autocompleteResult.getPredictions()) {
                            if (prediction.getTypes().contains("food")) {

                                food.add(prediction);
                            }
                        }
                        return food;
                    }
                })
                .flatMap(new Function<Prediction, ObservableSource<PlaceDetail>>() {
                    @Override
                    public ObservableSource<PlaceDetail> apply(Prediction prediction) throws Exception {
                        return streamFetchDetails(prediction.getPlaceId());
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
