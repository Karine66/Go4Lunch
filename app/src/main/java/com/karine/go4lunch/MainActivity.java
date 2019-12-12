package com.karine.go4lunch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_activity_coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.google_btn) Button mGoogleBtn;




    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
       // this.handleResponseAfterSignIn(requestCode,resultCode, data);
    }
    @OnClick(R.id.google_btn)
    public void onClickGoogleBtn (View v) {
        this.startSignInActivity();
    }

    //Show snack bar with message
    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar.make(coordinatorLayout, message,Snackbar.LENGTH_SHORT).show();
    }

    private void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(new
                        AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false, true)
                .build(),
                RC_SIGN_IN);
    }
//    //method that handles response after signin Activity close
//    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){
//        IdpResponse response = IdpResponse.fromResultIntent(data);
//        if (requestCode == RC_SIGN_IN) {
//            if (resultCode == RESULT_OK) {//sucess
//                showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed));
//            } else { //error
//                if (response == null) {
//                    showSnackBar(this.coordinatorLayout, getString(R.string.error_authentication_canceled));
//                } else if (response.getError() == ErrorCodes.NO_NETWORK) {
//                    showSnackBar(this.coordinatorLayout, getString(R.string.error_no_internet));
//                } else if (response.getError() == ErrorCodes.UNKNOWN_ERROR) {
//                    showSnackBar(this.coordinatorLayout, getString(R.string.error_unknown_error));
//                }
//            }
//        }
  //  }
}
