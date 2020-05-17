package com.karine.go4lunch.utils;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.karine.go4lunch.R;

import java.util.Calendar;
import java.util.Objects;

public class NotificationHelper extends ContextWrapper {

    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;
    private NotificationCompat.Builder notifBuilder;
    private PendingIntent pendingIntent;
    private String resto;
    private String address;
    //For Sound alarm
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();

        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
//        Objects.requireNonNull(mManager).notify(NOTIFICATION_TAG,NOTIF_ID, notifBuilder.build());
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(Intent intent) {
//        retrieveData(intent);
        return new NotificationCompat.Builder(getApplicationContext(), channelID)

                .setContentTitle("Alarm!")
//                .setContentText("Your AlarmManager is working.")
                .setSmallIcon(R.drawable.lunch_black)
                .setSound(alarmSound);


    }
//    public void retrieveData (Intent intent) {
//        if(intent !=null) {
//            Bundle bundle = intent.getExtras();
//            resto = bundle.getString("restoNotifName");
//            address = bundle.getString("restoNotifAddress");
//
//        }
//    }
}
