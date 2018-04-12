package com.willblaschko.android.alexa.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.beans.AlertBean;
import com.willblaschko.android.alexa.data.Event;
import com.willblaschko.android.alexa.receiver.AlertReceiver;
import com.willblaschko.android.alexa.utility.TimeUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlertService extends Service {

    private static final String TAG = "AlertService";
    private static final long SECOND_INTERVAL = 1000;
    private static final long LASTING_RING_TIME = 60 * 60 * 1000;

    private String mCacheDir = Environment.getExternalStorageDirectory() + "/AlexaAudioCache/";
    public AlertBinder mBinder = new AlertBinder();
    private List<String> mPlayIds;
    private Map<String, String> mPlayMap;

    private long mLoopCount;
    private long mLoopPauseInMilliSeconds;
    private String mToken;
    private String mType;
    private String mScheduledTime;
    private ArrayList<String> mAssetPlayOrder;
    private String mBackgroundAlertAsset;
    private List<AlertBean.AssetsBean> mAssets;
    private int mId;
    private List<String> mAssetIds;
    private List<String> mAssetUrls;

    // localUrl = /storage/emulated/0/AlexaVideoCache/*.mp3";
    public AlertService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onStartCommand: ======== " + bundle);
        //intent.putExtra("tag", "deleteAlert");
        String tag = intent.getStringExtra("tag");
        if (bundle != null && "deleteAlert".equals(tag)) {
            int deleteId = intent.getIntExtra("id", -1);
            String token = intent.getStringExtra("token");
            if (deleteId >= 0) {
                cancelAlertTask(deleteId, token);
            }
        } else if(bundle != null) {
            getAttrs(bundle);
            setAlertTask();
        }
        return START_STICKY;
    }

    private void cancelAlertTask(int deleteId, String token) {
        Log.d(TAG, "cancelAlertTask: id is" + deleteId);
        try {
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this, deleteId,
                    intent, 0);
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.cancel(pi);
            AlexaManager.getInstance(this).sendEvent(Event.getDeleteAlertSucceededEvent(token), null);
        } catch (Exception e) {
            AlexaManager.getInstance(this).sendEvent(Event.getDeleteAlertFailedEvent(token), null);
            e.printStackTrace();
        }
    }

    private void getAttrs(Bundle bundle) {
        mType = bundle.getString("type");
        mScheduledTime = bundle.getString("scheduledTime");
        mAssets = (List<AlertBean.AssetsBean>) bundle.getSerializable("assets");
        mAssetPlayOrder = bundle.getStringArrayList("assetPlayOrder");
        mLoopPauseInMilliSeconds = bundle.getLong("loopPauseInMilliSeconds");
        mLoopCount = /*bundle.getLong("loopCount")*/1;
        mBackgroundAlertAsset = bundle.getString("backgroundAlertAsset");
        mToken = bundle.getString("token");
        Log.d(TAG, "onStartCommand: type is " + (mAssets != null ? mAssets.size() : 0) + "--- " + mLoopPauseInMilliSeconds + "-- " + mToken);
        mPlayMap = new HashMap<>();
        mAssetIds = new ArrayList<>();
        mAssetUrls = new ArrayList<>();
        if (mAssets != null) {
            for (AlertBean.AssetsBean assetBean : mAssets) {
                mPlayMap.put(assetBean.getAssetId(), assetBean.getUrl());
                mAssetIds.add(assetBean.getAssetId());
                mAssetUrls.add(assetBean.getUrl());
            }
        }
        //  set an custome alarm
        mPlayIds = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = mPlayMap.entrySet();
        if (mAssetPlayOrder != null) {
            for (String assetPlay : mAssetPlayOrder) {
                if (mAssets != null) {
                    for (Map.Entry<String, String> playBean: entries) {
                        String playId = playBean.getKey();
                        if (assetPlay.equals(playId)) {
                            mPlayIds.add(playId);
                        }
                    }
                }
            }
        }
        saveData();
    }

    private void saveData() {
        LitePal.getDatabase();
        AlertBean alertBean = new AlertBean();
        alertBean.setToken(mToken);
        alertBean.setType(mType);
        alertBean.setScheduledTime(mScheduledTime);
//        alertBean.setAssets(mAssets);
        alertBean.setAssetIds(mAssetIds);
        alertBean.setAssetUrls(mAssetUrls);
        alertBean.setAssetPlayOrder(mAssetPlayOrder);
        alertBean.setBackgroundAlertAsset(mBackgroundAlertAsset);
        alertBean.setLoopCount(mLoopCount);
        alertBean.setLoopPauseInMilliSeconds(mLoopPauseInMilliSeconds);
        alertBean.save();
        mId = alertBean.getId();
    }

    private void setAlertTask() {
        Log.d(TAG, "setAlertTask: -------------" + PendingIntent.FLAG_CANCEL_CURRENT + "-" + PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("id", mId);
            PendingIntent sender = PendingIntent.getBroadcast(
                    this, mId, intent, 0);
            long alertTime = TimeUtil.getAlertTime(mScheduledTime);
            Log.d(TAG, "setAlertTask: scheduleTime is -->" + mScheduledTime + ",now is " + System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(alertTime);
            calendar.add(Calendar.SECOND, 0);

            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        } catch (Exception e) {
            Log.d(TAG, "onStartCommand: error occur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class AlertBinder extends Binder {

        private AlertBinder() {
        }

        public void setType(String type) {
            Log.d("AlertService", "setType: " + type);
        }

        public void setNewAlert() {
            Log.d(TAG, "setNewAlert: -----");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

}
