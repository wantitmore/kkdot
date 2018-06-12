package com.konka.alexa.alexalib.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.konka.alexa.alexalib.AlexaManager;
import com.konka.alexa.alexalib.beans.AlertBean;
import com.konka.alexa.alexalib.data.Event;
import com.konka.alexa.alexalib.service.AlertHandlerService;
import com.konka.alexa.alexalib.utility.TimeUtil;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AlertHandlerReceiver extends BroadcastReceiver {

    private static final String TAG = "AlertHandlerReceiver";

    private List<String> mPlayIds;
    private Map<String, String> mPlayMap;
    private Context mContext;

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

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        mContext = context;
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
        }
    }

    private void getAttrs(Bundle bundle) {
        mType = bundle.getString("type");

        mScheduledTime = bundle.getString("scheduledTime");
        mAssets = (List<AlertBean.AssetsBean>) bundle.getSerializable("assets");
        mAssetPlayOrder = bundle.getStringArrayList("assetPlayOrder");
        mLoopPauseInMilliSeconds = bundle.getLong("loopPauseInMilliSeconds");
        mLoopCount = bundle.getLong("loopCount");
        mBackgroundAlertAsset = bundle.getString("backgroundAlertAsset");
        mToken = bundle.getString("token");
        Log.d(TAG, "onStartCommand: type is " + mType + "--- " + mLoopPauseInMilliSeconds + "-- " + mToken);
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
        //handle snooze alert
        List<AlertBean> alertBeans = DataSupport.where("token = ?", mToken).find(AlertBean.class);
        if (alertBeans != null && alertBeans.size() > 0) {
            if (!TextUtils.equals(mType, "ALARM")) {
                return;
            }
            AlertBean alertBean = alertBeans.get(0);
            if (alertBean.isActive()) {
                alertBean.setScheduledTime(mScheduledTime);
                alertBean.setActive(false);
                alertBean.save();
                mId = alertBean.getId();
                Log.d(TAG, "getAttrs: -------------mId is " + mId);
                Intent intent = new Intent(mContext, AlertHandlerService.class);
                intent.putExtra("active", true);
                mContext.startService(intent);

            }
        }else {
            saveData();
        }
        setAlertTask();
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
            Intent intent = new Intent(mContext, AlertHandlerService.class);
            intent.putExtra("id", mId);
            PendingIntent sender = PendingIntent.getService(
                    mContext, mId, intent, 0);
            long alertTime = TimeUtil.getAlertTime(mScheduledTime);
            Log.d(TAG, "setAlertTask: scheduleTime is -->" + mScheduledTime + ",now is " + System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(alertTime/*System.currentTimeMillis()*/);
//            calendar.add(Calendar.SECOND, 15);

            AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        } catch (Exception e) {
            Log.d(TAG, "onStartCommand: error occur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cancelAlertTask(int deleteId, String token) {
        Log.d(TAG, "cancelAlertTask: id is" + deleteId);
        try {
            Intent intent = new Intent(mContext, AlertHandlerService.class);
            PendingIntent pi = PendingIntent.getService(mContext, deleteId,
                    intent, 0);
            AlarmManager am = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pi);
            AlertBean alertBean = DataSupport.find(AlertBean.class, deleteId);
            boolean active = alertBean.isActive();
            Log.d(TAG, "cancelAlertTask: isActive " + active);
            if (active) {
                Log.d(TAG, "cancelAlertTask: ------------------------");
                intent.putExtra("active", true);
                mContext.startService(intent);
            }
            Log.d(TAG, "cancelAlertTask: for test");
            DataSupport.delete(AlertBean.class, deleteId);
            AlexaManager.getInstance(mContext).sendEvent(Event.getDeleteAlertSucceededEvent(token), null);
        } catch (Exception e) {
            AlexaManager.getInstance(mContext).sendEvent(Event.getDeleteAlertFailedEvent(token), null);
            e.printStackTrace();
        }
    }



}
