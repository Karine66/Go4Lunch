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
import com.karine.go4lunch.models.User;

import java.util.List;

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

    private Disposable mDisposable;

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
        //for retrieve name
       mWorkmatesName.setText(users.getUsername() + " " +users.getPlaceId());
       //for retrieve user photo
        if (users.getUrlPicture() != null && !users.getUrlPicture().isEmpty()) {
            glide.load(users.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(mWorkmatesPhoto);
        } else {
            mWorkmatesPhoto.setImageResource(R.drawable.no_picture);
        }
    }

    private void executeHttpRequestWithRetrofit() {
        this.mDisposable = NYTStreams.streamFetchMostPopular("viewed")
                .subscribeWith(new DisposableObserver<MostPopular>() {
                    @Override
                    public void onNext(MostPopular section) {
                        NYTResultsAPI nytResultsAPI = NYTResultsAPI.createResultsApiFromMostPopular(section);
                        updateUI(nytResultsAPI);
                    }

                    @Override
                    public void onComplete() {

                        Log.d("ON_Complete", "Test onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onErrorMP", Log.getStackTraceString(e));
                    }
                });
    }




}

