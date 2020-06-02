package com.karine.go4lunch.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.karine.go4lunch.API.UserHelper;
import com.karine.go4lunch.R;
import com.karine.go4lunch.models.User;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.Collections;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.karine.go4lunch.utils.FirebaseUtils.getCurrentUser;
import static com.karine.go4lunch.utils.FirebaseUtils.onFailureListener;

;


public class LoginActivity extends AppCompatActivity {
    //Declarations
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
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    @OnClick(R.id.google_btn)
    public void onClickGoogleBtn(View v) {
        this.startSignInActivitywithGoogle();
    }

    @OnClick(R.id.facebook_btn)
    public void onClickFacebookBtn(View v) {
        this.startSignInActivitywithFacebook();
    }

    /**
     * Create user in Firebase for Google
     */
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

    /**
     * Create user in Firebase for Facebook
     */
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

    /**
     * Http request that create user in firestore
     */
    private void createUserInFirestore() {

        String urlPicture = (getCurrentUser().getPhotoUrl() != null) ?
                getCurrentUser().getPhotoUrl().toString() : null;
        String userName = getCurrentUser().getDisplayName();
        String uid = getCurrentUser().getUid();
        UserHelper.getUser(uid).addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            if (user != null) {
                UserHelper.createUser(uid, userName, urlPicture, user.getPlaceId(), user.getLike()).addOnFailureListener(onFailureListener());
            } else {
                UserHelper.createUser(uid, userName, urlPicture, null, null).addOnFailureListener(onFailureListener());
            }
        });
    }

    /**
     * method that handles response after sign in Activity close
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {//sucess
                StyleableToast.makeText(this, getString(R.string.succeed_connection), R.style.personalizedToast).show();
                this.createUserInFirestore();
                Intent loginIntent = new Intent(this, MainPageActivity.class);
                startActivity(loginIntent);
            } else { //error
                if (response == null) {
                    StyleableToast.makeText(this, getString(R.string.auth_canceled), R.style.personalizedToast).show();
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    StyleableToast.makeText(this, getString(R.string.no_internet), R.style.personalizedToast).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    StyleableToast.makeText(this, getString(R.string.unknown_error), R.style.personalizedToast).show();
                }
            }
        }
    }
}
