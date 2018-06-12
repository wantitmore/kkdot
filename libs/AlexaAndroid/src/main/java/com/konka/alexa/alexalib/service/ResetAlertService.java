package com.konka.alexa.alexalib.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.konka.alexa.alexalib.AlexaManager;
import com.konka.alexa.alexalib.beans.AlertBean;
import com.konka.alexa.alexalib.beans.UnSendBean;
import com.konka.alexa.alexalib.data.Event;
import com.konka.alexa.alexalib.utility.AlertUtil;
import com.konka.alexa.alexalib.utility.TimeUtil;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

/**
 * Created by user001 on 2018-4-11.
 */

public class ResetAlertService extends IntentService {

    private int count4Test = 1;
    private static final String TAG = "ResetAlertService";
    private static final long ELAPSE_TIME = 30 * 60 * 1000;

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
                String token = alertBean.getToken();
                String scheduledTime = alertBean.getScheduledTime();
                long alertTime = TimeUtil.getAlertTime(scheduledTime);
                long elapsedTime = System.currentTimeMillis() - alertTime;

                Log.d(TAG, "onHandleIntent: elap is " + elapsedTime + ", alertTIme is " + scheduledTime + ", restartTime is " + System.currentTimeMillis());
                if (elapsedTime > ELAPSE_TIME) {
                    //discard alert
                    DataSupport.delete(AlertBean.class, id);
                    if (AlertUtil.isNetworkConnected(this)) {
                        AlexaManager.getInstance(this).sendEvent(Event.getAlertStoppedEvent(token), null);
                    } else {
                        List<UnSendBean> unSendBeans = DataSupport.where("token = ?", token).find(UnSendBean.class);
                        if (unSendBeans != null && unSendBeans.size() > 0) {
                            UnSendBean unSendBean = unSendBeans.get(0);
                            unSendBean.setType("AlertStopped");
                            unSendBean.save();
                        } else {
                            UnSendBean unSendBean = new UnSendBean();
                            unSendBean.setType("AlertStopped");
                            unSendBean.setToken(token);
                            unSendBean.save();
                        }
                    }
                } else if (elapsedTime <= 0) {
                    Intent alertIntent = new Intent(this, AlertHandlerService.class);
                    alertIntent.putExtra("id", id);
                    PendingIntent sender = PendingIntent.getService(
                            this, id, alertIntent, 0);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(/*System.currentTimeMillis()*/alertTime);
//                                    calendar.add(Calendar.SECOND, 15 * (count4Test++));

                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                } else {
                    Intent alertIntent = new Intent(this, AlertHandlerService.class);
                    alertIntent.putExtra("id", id);
                    startService(alertIntent);
                }
            } catch (Exception e) {
                Log.d("ResetAlertService", "onStartCommand: error occur: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
