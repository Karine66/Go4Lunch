package com.karine.go4lunch.controllers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;
import com.karine.go4lunch.R;
import com.karine.go4lunch.utils.AlertReceiver;
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

        SharedPreferences saveFavPrefs = getSharedPreferences("cancelAlarm", MODE_PRIVATE);;
        mAlarmOff.setSelected(saveFavPrefs.getBoolean("cancelAlarm", true));
    }

    public void onTimeSet() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 17);
        c.set(Calendar.MINUTE,54);
        c.set(Calendar.SECOND, 0);

        startAlarm(c);
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

                SharedPreferences sharedPref = getSharedPreferences("cancelAlarm", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (mAlarmOff.isEnabled()){
                    editor.putBoolean("alarmOff", true);
                    editor.apply();

                }
                else{

                    editor.putBoolean("alarmOff", false);
                    editor.apply();
                }
            }
        });
    }


    //For button alarm on
    public void alarmOn(){
        mAlarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeSet();
                StyleableToast.makeText(getApplicationContext(),"Alarm activated for 12 h",R.style.personalizedToast).show();

            }
        });
    }
}
