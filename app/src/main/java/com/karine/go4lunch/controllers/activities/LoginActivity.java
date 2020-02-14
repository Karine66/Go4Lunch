package com.karine.go4lunch.controllers.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.karine.go4lunch.API.UserHelper;
import com.karine.go4lunch.R;
import com.karine.go4lunch.Utils.FirebaseUtils;
import com.karine.go4lunch.models.User;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        //For Hide Action Bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.karine.go4lunch",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
//
//        }

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
        if(FirebaseUtils.getCurrentUser() != null) {
            String urlPicture = (FirebaseUtils.getCurrentUser().getPhotoUrl() !=null) ?
                    FirebaseUtils.getCurrentUser().getPhotoUrl().toString() : null;
            String userName = FirebaseUtils.getCurrentUser().getDisplayName();
            String uid = FirebaseUtils.getCurrentUser().getUid();

            UserHelper.createUser(uid, userName, urlPicture).addOnFailureListener(onFailureListener());
        }
    }

    //method that handles response after signin Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {//sucess
                Toast.makeText(this,"Connection succeed", Toast.LENGTH_SHORT).show();
                this.createUserInFirestore();
                Intent loginIntent = new Intent(this, MainPageActivity.class);
                startActivity(loginIntent);
            } else { //error
                if (response == null) {
                    Toast.makeText(this,"Error authentication canceled", Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this,"Error no internet", Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this,"Error unknown error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
