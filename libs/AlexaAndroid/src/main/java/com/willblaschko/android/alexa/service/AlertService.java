package com.willblaschko.android.alexa.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.willblaschko.android.alexa.data.Directive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AlertService extends Service {

    private static final String TAG = "AlertService";
    public AlertBinder mBinder = new AlertBinder();

    public AlertService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ======== ");

        Bundle bundle = intent.getExtras();
        List<Directive.Payload.AssetsBean> assets;
        if (bundle != null) {
            String type = bundle.getString("type");
            String scheduledTime = bundle.getString("scheduledTime");
            assets = (List<Directive.Payload.AssetsBean>) bundle.getSerializable("assets");
            ArrayList<String> assetPlayOrder = bundle.getStringArrayList("assetPlayOrder");
            long loopPauseInMilliSeconds = bundle.getLong("loopPauseInMilliSeconds");
            long loopCount = bundle.getLong("loopCount");
            String backgroundAlertAsset = bundle.getString("backgroundAlertAsset");
            Log.d(TAG, "onStartCommand: type is " + (assets != null ? assets.size() : 0) + "--- " + loopPauseInMilliSeconds);
            //  set an custome alarm
            List<String> playUrls = new ArrayList<>();
            if (assetPlayOrder != null) {
                for (String assetPlay : assetPlayOrder) {
                    if (assets != null) {
                        for (Directive.Payload.AssetsBean assetBean : assets) {
                            if (!TextUtils.isEmpty(assetPlay) && assetPlay.equals(assetBean.getAssetId())) {
                                Log.d(TAG, "onStartCommand: id is " + assetPlay);
                                playUrls.add(assetBean.getUrl());
                            }
                        }
                    }
                }
            }
            try {
                final MediaPlayer player = new MediaPlayer();

                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(this, Uri.parse(playUrls.get(0)));
                player.prepareAsync();
                final Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                Log.d(TAG, "onPrepared: play start");
                                player.start();
                            }
                        });
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {

                            }
                        });
                    }
                };
                Log.d(TAG, "onStartCommand: time alarm is " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(scheduledTime));
                 timer.schedule(task, /*new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).parse(scheduledTime)*/5000);
            } catch (Exception e) {
                Log.d(TAG, "onStartCommand: error occur: " + e.getMessage());
                e.printStackTrace();
            }

        }
        return super.onStartCommand(intent, flags, startId);
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
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
}
