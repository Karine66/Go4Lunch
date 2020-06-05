package com.karine.go4lunch.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.karine.go4lunch.R;
import com.karine.go4lunch.controllers.activities.RestaurantActivity;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;
import com.karine.go4lunch.models.User;
import com.karine.go4lunch.utils.Go4LunchStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class WorkmatesViewHolder extends RecyclerView.ViewHolder {
    /**
     * Declarations
     */
    @BindView(R.id.workmates_photo)
    ImageView mWorkmatesPhoto;
    @BindView(R.id.workmates_name)
    TextView mWorkmatesName;

    private Disposable mDisposable;
    private String restoName;
    private String idResto;
    private PlaceDetail detail;
    private String userName;


    public WorkmatesViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        //for retrieve restaurant sheet on click workmates
        itemView.setOnClickListener(v -> {
            if (detail != null) {
                Intent intent = new Intent(v.getContext(), RestaurantActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("placeDetailsResult", detail.getResult());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
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

        Log.d("idRestoUser", "idRestoUsers" + " " + idResto);
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
                            mWorkmatesName.setText(userName + " " + itemView.getContext().getString(R.string.eatWorkmates) + " " + restoName);
                            Log.d("OnCompleteRestoName", "restoName" + idResto);
                        } else {
                            mWorkmatesName.setText(userName + " " + itemView.getContext().getString(R.string.no_decided));
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

