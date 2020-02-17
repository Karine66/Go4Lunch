package com.karine.go4lunch.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.karine.go4lunch.R;

import com.karine.go4lunch.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkmatesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.workmates_photo)
    ImageView mWorkmatesPhoto;
    @BindView(R.id.workmates_name)
    TextView mWorkmatesName;

    public WorkmatesViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

   public void updateWithDetails(User users) {

//        //mWorkmatesName.setText(Objects.requireNonNull(FirebaseUtils.getCurrentUser()).getDisplayName());
       mWorkmatesName.setText(users.getUsername());
//        if(FirebaseUtils.getCurrentUser() != null) {
//            //Get Picture Url from Firebase
//            if(FirebaseUtils.getCurrentUser().getPhotoUrl() != null) {
//                        glide.load(FirebaseUtils.getCurrentUser().getPhotoUrl())
//                        .apply(RequestOptions.circleCropTransform())
//                        .into(mWorkmatesPhoto);
//            }
//
//            //Get Username from Firebase
//            String username = TextUtils.isEmpty(FirebaseUtils.getCurrentUser().getDisplayName()) ?
//                    ("no username found") :
//                    FirebaseUtils.getCurrentUser().getDisplayName();
//            mWorkmatesName.setText(username);
//            Log.d("workmatesName", username);
//        }
    }
}

