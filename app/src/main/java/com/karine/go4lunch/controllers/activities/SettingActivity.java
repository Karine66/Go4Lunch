package com.karine.go4lunch.controllers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.karine.go4lunch.R;
import com.karine.go4lunch.Utils.AlertReceiver;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.alarmOn)
    Button mAlarmOn;
    @BindView(R.id.alarmOff)
    Button mAlarmOff;

    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        this.alarmOn();
        this.alarmOff();

    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

    }

    //For Cancel Alarm
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        Objects.requireNonNull(alarmManager).cancel(pendingIntent);

    }
    //For button alarm off
    public void alarmOff(){
        mAlarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
                StyleableToast.makeText(getApplicationContext(),"Alarm canceled",R.style.personalizedToast).show();
            }
        });
    }
    //For button alarm on
    public void alarmOn(){
        mAlarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlarm(c);
                StyleableToast.makeText(getApplicationContext(),"Alarm activated",R.style.personalizedToast).show();

            }
        });
    }
}
