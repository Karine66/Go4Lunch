package com.karine.go4lunch.utils;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karine.go4lunch.API.UserHelper;
import com.karine.go4lunch.R;
import com.karine.go4lunch.models.PlaceDetailsAPI.PlaceDetail;
import com.karine.go4lunch.models.User;

import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.karine.go4lunch.utils.DatesAndHours.getCurrentTime;

public class AlertReceiver extends BroadcastReceiver {


    private String userIdNotif;
    private PlaceDetail detail;
    private String restoNotifName;
    private Disposable mDisposable;
    private String restoNotifAddress;
    private String placeId;

    private String nameNotif;
    private String notifMessage;
    private Context context;
    private NotificationHelper notificationHelper;
    private Integer currentTime;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context= context;
//            NotificationHelper notificationHelper = new NotificationHelper(context);
//        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(notifMessage);
//        notificationHelper.getManager().notify(1, nb.build());

        UserHelper.getUser(Objects.requireNonNull(FirebaseUtils.getCurrentUser()).getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                com.karine.go4lunch.models.User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    if (!user.getPlaceId().isEmpty()) {

                        userIdNotif = user.getPlaceId();
                        executeHttpRequestWithRetrofit();
                    Log.d("TestNotifId", userIdNotif);
                    }

                }

            }
        });

    }
    //request RXJava for retrieve restaurant name and restaurant address
    private void executeHttpRequestWithRetrofit() {
        this.mDisposable = Go4LunchStream.streamFetchDetails(userIdNotif)
                .subscribeWith(new DisposableObserver<PlaceDetail>() {

                    @Override
                    public void onNext(PlaceDetail placeDetail) {
                        
                        detail = placeDetail;
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete() {

                        if (userIdNotif != null) {
                            restoNotifName = detail.getResult().getName();
                            restoNotifAddress = detail.getResult().getVicinity();
                            workmatesNotif(userIdNotif);



//                            if(nameNotif !=null) {
//                                notifMessage = ("You're lunching at" + " " + restoNotifName + " " +
//                                        restoNotifAddress + " " + "with" + " " + nameNotif);
//                            } else {
//                                notifMessage = ("You're lunching at" + " " + restoNotifName + " " +
//                                        restoNotifAddress + " " + "alone");
//                            }
//                            NotificationHelper notificationHelper = new NotificationHelper(context);
//                            NotificationCompat.Builder nb = notificationHelper.getChannelNotification(notifMessage);
//                            notificationHelper.getManager().notify(1, nb.build());
                           
                            Log.d("RestoNameNotif",  restoNotifName +" "+ restoNotifAddress+" "+nameNotif + " " + notifMessage);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onErrorRestoNotif", Log.getStackTraceString(e));
                    }
                });
    }

    //For Retrieve workmates who chose this restaurant

    private void workmatesNotif(String userIdNotif) {

        UserHelper.getUsersCollection()
                .whereEqualTo("placeId", userIdNotif)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                Log.d("workmatesNotif", documentSnapshot.getId() + " " + documentSnapshot.getData());
                                nameNotif = String.valueOf(documentSnapshot.get("username"));
                                Log.d("nameNotif", Objects.requireNonNull(nameNotif));

                                if(nameNotif !=null) {
                                    notifMessage = (context.getString(R.string.lunch_at) + " " + restoNotifName + " " +
                                            restoNotifAddress +context.getString(R.string.with) +  nameNotif);
                                } else {
                                    notifMessage = (context.getString(R.string.lunch_at) + " " + restoNotifName + " " +
                                            restoNotifAddress + " " + context.getString(R.string.alone));
                                }
                                NotificationHelper notificationHelper = new NotificationHelper(context);
                                NotificationCompat.Builder nb = notificationHelper.getChannelNotification(notifMessage);
                                notificationHelper.getManager().notify(1, nb.build());
                            }

                        }else{
                            Log.e("numberMatesError", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}




