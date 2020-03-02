package com.karine.go4lunch.views;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.karine.go4lunch.R;
import com.karine.go4lunch.Utils.Go4LunchStream;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetailsResult;
import com.karine.go4lunch.models.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;

public class WorkmatesViewHolder extends RecyclerView.ViewHolder {
    /**
     * Déclarations
     */
    @BindView(R.id.workmates_photo)
    ImageView mWorkmatesPhoto;
    @BindView(R.id.workmates_name)
    TextView mWorkmatesName;
    private PlaceDetail placeDetail;
    private Disposable mDisposable;
    private String placeId;


    private String restoName;
    private String name;
    private String idResto;
    private User users;
    private PlaceDetailsResult result;
    private PlaceDetail detail;
    private String userName;


    public WorkmatesViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    /**
     * For update usernames and photos
     *
     * @param users
     * @param glide
     */
    @SuppressLint("SetTextI18n")
    public void updateWithDetails(User users, RequestManager glide) {
        //for retrieve name and id resto for request
        userName = users.getUsername();
        idResto = users.getPlaceId();
        Log.d("idRestoUser", "idRestoUsers"+ " " +idResto);
        executeHttpRequestWithRetrofit();
       //for retrieve user photo
        if (users.getUrlPicture() != null && !users.getUrlPicture().isEmpty()) {
            glide.load(users.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(mWorkmatesPhoto);
        } else {
            mWorkmatesPhoto.setImageResource(R.drawable.no_picture);
        }
    }

    /**
     * request for restrieve restaurant name with id
     */

    private void executeHttpRequestWithRetrofit() {
        this.mDisposable = Go4LunchStream.streamFetchDetails(idResto)
                .subscribeWith(new DisposableObserver<PlaceDetail>() {

                    @Override
                    public void onNext(PlaceDetail placeDetail) {
                    detail = placeDetail;
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete() {

                        if (idResto != null) {
                            restoName = detail.getResult().getName();
                            mWorkmatesName.setText(userName + " " + "eat at" + " " + restoName);
                            Log.d("OnCompleteRestoName", "restoName" + idResto);
                        } else {
                            mWorkmatesName.setText(userName + " " + "hasn't decided yet");
                            Log.d("RestoName", "noResto" + userName);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onErrorWorkmates", Log.getStackTraceString(e));
                    }
                });
    }
}

