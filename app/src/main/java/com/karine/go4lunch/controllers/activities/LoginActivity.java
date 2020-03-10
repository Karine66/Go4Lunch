package com.karine.go4lunch.controllers.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.AuthUI;;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.karine.go4lunch.API.UserHelper;
import com.karine.go4lunch.R;
import com.karine.go4lunch.models.User;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.Collections;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.karine.go4lunch.Utils.FirebaseUtils.getCurrentUser;
import static com.karine.go4lunch.Utils.FirebaseUtils.onFailureListener;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.main_activity_coordinator_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.google_btn)
    Button mGoogleBtn;
    @BindView(R.id.facebook_btn)
    Button mFacebookBtn;

    private static final int RC_SIGN_IN = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
       this.handleResponseAfterSignIn(requestCode,resultCode, data);
    }
    @OnClick(R.id.google_btn)
    public void onClickGoogleBtn (View v) {
        this.startSignInActivitywithGoogle();
    }
    @OnClick(R.id.facebook_btn)
    public void onClickFacebookBtn(View v) {
       this.startSignInActivitywithFacebook();
    }

//Create user in Firebase
    private void startSignInActivitywithGoogle() {
        startActivityForResult(
                AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(new
                        AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false, true)
                .build(),
                RC_SIGN_IN);
    }

    private void startSignInActivitywithFacebook() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(new
                                AuthUI.IdpConfig.FacebookBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    //Http request that create user in firestore
    private void createUserInFirestore() {

            String urlPicture = (getCurrentUser().getPhotoUrl() != null) ?
                    getCurrentUser().getPhotoUrl().toString() : null;
            String userName = getCurrentUser().getDisplayName();
            String uid = getCurrentUser().getUid();
            UserHelper.getUser(uid).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        UserHelper.createUser(uid, userName, urlPicture, user.getPlaceId()).addOnFailureListener(onFailureListener());
                    }else{
                        UserHelper.createUser(uid, userName, urlPicture, null).addOnFailureListener(onFailureListener());
                    }
                }


            });
        }



    //method that handles response after signin Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {//sucess
                StyleableToast.makeText(this,"Connection succeed", R.style.personalizedToast).show();
                this.createUserInFirestore();
                Intent loginIntent = new Intent(this, MainPageActivity.class);
                startActivity(loginIntent);
            } else { //error
                if (response == null) {
                    StyleableToast.makeText(this,"Error authentication canceled", R.style.personalizedToast).show();
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    StyleableToast.makeText(this,"Error no internet", R.style.personalizedToast).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    StyleableToast.makeText(this,"Error unknown error", R.style.personalizedToast).show();
                }
            }
        }
    }
}
