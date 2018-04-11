package com.willblaschko.android.alexa.receiver;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.willblaschko.android.alexa.beans.AlertBean;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

/**
 * Created by user001 on 2018-4-11.
 */

public class ResetAlertService extends IntentService {

    private int count4Test = 1;
    private static final String TAG = "ResetAlertService";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public ResetAlertService() {
        super("ResetAlertService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: ");
        List<AlertBean> allAlertBeans = DataSupport.findAll(AlertBean.class);
        Log.d(TAG, "onHandleIntent: all alert size is " + allAlertBeans.size());
        for (AlertBean alertBean : allAlertBeans) {
            try {
                int id = alertBean.getId();
                Intent alertIntent = new Intent(this, AlertReceiver.class);
                alertIntent.putExtra("id", id);
                PendingIntent sender = PendingIntent.getBroadcast(
                        this, id, alertIntent, 0);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.SECOND, 15 * (count4Test++));

                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            } catch (Exception e) {
                Log.d("ResetAlertService", "onStartCommand: error occur: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
