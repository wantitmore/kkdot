package com.willblaschko.android.alexa.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.willblaschko.android.alexa.data.Directive;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class AlertService extends Service {

    private static final String TAG = "AlertService";
    public AlertBinder mBinder = new AlertBinder();
    private List<String> mPlayIds;
    private MediaPlayer mPlayer;
    private int mPlayPosition;
    private long mLoopCount;
    private long mRealLoopCount = 1;
    private Map<String, String> mPlayMap;
    private String mCacheDir = Environment.getExternalStorageDirectory() + "/AlexaAudioCache/";
    private TimerTask mTask;
    private Timer timer;
    private static final long SECOND_INTERVAL = 1000;
    private long mLoopPauseInMilliSeconds;

    // localUrl = /storage/emulated/0/AlexaVideoCache/*.mp3";
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
            mLoopPauseInMilliSeconds = bundle.getLong("loopPauseInMilliSeconds");
            mLoopCount = /*bundle.getLong("loopCount")*/3;
            String backgroundAlertAsset = bundle.getString("backgroundAlertAsset");
            Log.d(TAG, "onStartCommand: type is " + (assets != null ? assets.size() : 0) + "--- " + mLoopPauseInMilliSeconds);
            mPlayMap = new HashMap<>();
            if (assets != null) {
                for (Directive.Payload.AssetsBean assetBean : assets) {
                    mPlayMap.put(assetBean.getAssetId(), assetBean.getUrl());
                }
            }
            //  set an custome alarm
            mPlayIds = new ArrayList<>();
            Set<Map.Entry<String, String>> entries = mPlayMap.entrySet();
            if (assetPlayOrder != null) {
                for (String assetPlay : assetPlayOrder) {
                    if (assets != null) {
                        for (Map.Entry<String, String> playBean: entries) {
                            String playId = playBean.getKey();
                            if (assetPlay.equals(playId)) {
                                mPlayIds.add(playId);
                            }
                        }
                    }
                }
            }
            try {
                timer = new Timer();
                mTask = new TimerTask() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: -------------------------------------");
                        playAlert(mPlayPosition);

                    }
                };
                Log.d(TAG, "onStartCommand: time alarm is " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(scheduledTime));
                timer.schedule(mTask, /*new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).parse(scheduledTime)*/5000);
            } catch (Exception e) {
                Log.d(TAG, "onStartCommand: error occur: " + e.getMessage());
                e.printStackTrace();
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void playAlert(int position) {
        cancelTask();
        String playId = mPlayIds.get(position);
        String playUrl = mPlayMap.get(playId);
        Log.d(TAG, "playId size is " + (mPlayIds != null ? mPlayIds.size() : "null"));
        if (mPlayer != null) {
            mPlayer.reset();
            mPlayer = null;
        }
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            File file = new File(mCacheDir + playId + ".mp3");
            Log.d(TAG, "playAlert: file path is " + file.getAbsolutePath());
            if (file.exists()) {
                Log.d(TAG, "playAlert: local--");
                mPlayer.setDataSource(this, Uri.fromFile(file));
            } else {
                Log.d(TAG, "playAlert: url--");
                downloadFile(playId, playUrl);
                mPlayer.setDataSource(this, Uri.parse(playUrl));
            }
        } catch (IOException e) {
            Log.e(TAG, "playAlert: exception is " + e.getMessage());
            e.printStackTrace();
        }
        mPlayer.prepareAsync();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "onPrepared: play start");
                mPlayer.start();
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if (mRealLoopCount < mLoopCount) {
                    Log.d(TAG, "onCompletion: realLoopCount is " + mRealLoopCount);
                    if (mPlayIds.size() - 1 > mPlayPosition) {
                        mPlayPosition++;
                        Log.d(TAG, "onCompletion: position is " + mPlayPosition);

                    } else {
                        Log.d(TAG, "onCompletion: ===============");
                        new CountDownTimer(mLoopPauseInMilliSeconds, SECOND_INTERVAL) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                // TODO Auto-generated method stub
                                Log.d(TAG, "onTick: --------------------------");
                            }

                            @Override
                            public void onFinish() {
                                Log.d(TAG, "onFinish: start to next loop");
                                mRealLoopCount++;
                                mPlayPosition = 0;
                            }
                        }.start();
                    }
                    playAlert(mPlayPosition);
                } else {
                    mRealLoopCount = 1;
                }
            }
        });
        mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.d(TAG, "onBufferingUpdate: -----------");
            }
        });
    }

    private void cancelTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            timer = null;
        }
    }

    private void downloadFile(String playId, String playUrl) {
        FileDownloader.getImpl().create(playUrl).setPath(mCacheDir + playId + ".mp3")
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.d(TAG, "completed: download finish...");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.d(TAG, "error: download fail- " + e.getMessage());
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();
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
