package com.karine.go4lunch.controllers.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.karine.go4lunch.R;
import com.karine.go4lunch.utils.AlertReceiver;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {

    //Declaration
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
        //for retrieve statut color button
        if (mAlarmOn.isEnabled() && mAlarmOff.isEnabled()) {
            mAlarmOn.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
        } else if (!mAlarmOn.isEnabled() && !mAlarmOff.isEnabled()) {
            mAlarmOn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    /**
     * For set time of notifications
     */
    public void onTimeSet() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        startAlarm(c);
    }

    /**
     * For notification
     *
     * @param c
     */
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    /**
     * For cancel alarm
     */
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        Objects.requireNonNull(alarmManager).cancel(pendingIntent);
    }

    /**
     * For button alarm off
     */
    public void alarmOff() {
        mAlarmOff.setOnClickListener(v -> {

            if (mAlarmOff.isEnabled()) {
                cancelAlarm();
                mAlarmOff.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                mAlarmOn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                StyleableToast.makeText(getApplicationContext(), getString(R.string.alarm_canceled), R.style.personalizedToast).show();
            } else if (!mAlarmOff.isEnabled()) {
                onTimeSet();
                mAlarmOff.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mAlarmOn.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                StyleableToast.makeText(getApplicationContext(), getString(R.string.alarm_activated), R.style.personalizedToast).show();
            }
            //for save preferences
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("alarmOff", mAlarmOff.isEnabled());
            editor.apply();
        });
    }

    /**
     * For button alarm on
     */
    public void alarmOn() {
        mAlarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onTimeSet();
                StyleableToast.makeText(getApplicationContext(), getString(R.string.alarm_activated), R.style.personalizedToast).show();

                if (mAlarmOn.isEnabled()) {
                    mAlarmOn.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                    mAlarmOff.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else if (!mAlarmOn.isEnabled()) {
                    mAlarmOn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mAlarmOff.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                }
                //For save preferences
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("alarmOn", mAlarmOn.isEnabled());
                editor.apply();
            }
        });
    }
}
