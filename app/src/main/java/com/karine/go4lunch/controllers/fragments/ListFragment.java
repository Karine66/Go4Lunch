package com.karine.go4lunch.controllers.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karine.go4lunch.R;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    public Disposable mDisposable;



    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

//       executeHttpRequestWithRetrofit();
    }
//    /**
//     * HTTP request RX Java for restaurants
//     */
//    private void executeHttpRequestWithRetrofit() {
//
//        this.mDisposable = Go4LunchStream.streamFetchRestaurants(loc, 5000, "restaurant")
//                .subscribeWith(new DisposableObserver<GoogleApi>() {
//
//
//                    private List<ResultSearch> resultList = new ArrayList<>();
//
//                    @Override
//                    public void onNext(GoogleApi mResults) {
//                        Log.d("TestonNextList", mResults.toString());
//                        resultList.addAll(mResults.getResults());
//                        Log.d("TestonNextListSize", String.valueOf(resultList.size()));
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                        for (ResultSearch res : resultList){
//                            LatLng latLng = new LatLng(res.getGeometry().getLocation().getLat(),
//                                    res.getGeometry().getLocation().getLng()
//                            );
//                            res.getName();
//
//                        }
//
//                        Log.d("TestOnComleteList", String.valueOf(resultList.size()));
//
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("onErrorRestaurantsList", Log.getStackTraceString(e));
//                    }
//                });

//}
}
