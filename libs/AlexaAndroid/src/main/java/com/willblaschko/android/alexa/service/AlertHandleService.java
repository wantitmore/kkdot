package com.willblaschko.android.alexa.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.beans.AlertBean;
import com.willblaschko.android.alexa.data.Event;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlertHandleService extends Service {

    private static final String TAG = "AlertHandleService";
    private static final long SECOND_INTERVAL = 1000;
    private static final long LASTING_RING_TIME = 60 * 60 * 1000;

    private String mToken;
    private String mType;
    private String mScheduledTime;
    private List<AlertBean.AssetsBean> mAssets;
    private List<String> mAssetPlayOrder;
    private String mBackgroundAlertAsset;
    private long mLoopCount;
    private long mLoopPauseInMilliSeconds;

    private Map<String, String> mPlayMap;
    private List<String> mPlayIds;
    private MediaPlayer mPlayer;
    private String mCacheDir = Environment.getExternalStorageDirectory() + "/AlexaAudioCache/";
    private long mRealLoopCount = 1;
    private int mPlayPosition;
    private long mStartAlertTime;
    private List<String> mAssetUrls;
    private List<String> mAssetIds;
    private boolean isStartEvent;
    private int mId;
    public AlertHandleService.AlertBinder mBinder = new AlertHandleService.AlertBinder();

    public AlertHandleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            boolean active = intent.getBooleanExtra("active", false);
            if (active) {
                stopPlayer();
            } else {
                getData(intent);
                mStartAlertTime = System.currentTimeMillis();
                isStartEvent = true;
                playAlert(0);
            }
        }
        return START_STICKY;
    }

    public class AlertBinder extends Binder {

        private final AlertHandleService service;

        private AlertBinder() {
            service = AlertHandleService.this;
        }

        public void setType(String type) {
            Log.d("AlertService", "setType: " + type);
        }

        public void stopPlayer() {
            Log.d(TAG, "stopPlayer: -----");
            service.stopPlayer();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    private void getData(Intent intent) {
        mId = intent.getIntExtra("id", -1);
        if (mId <= 0) {
            return;
        }
        mRealLoopCount = 1;
        AlertBean alertBean = DataSupport.find(AlertBean.class, mId);
        mToken = alertBean.getToken();
        mType = alertBean.getType();
        mScheduledTime = alertBean.getScheduledTime();
//        mAssets = alertBean.getAssets();
        mAssetIds = alertBean.getAssetIds();
        mAssetUrls = alertBean.getAssetUrls();
        mAssetPlayOrder = alertBean.getAssetPlayOrder();
        mBackgroundAlertAsset = alertBean.getBackgroundAlertAsset();
        mLoopCount = alertBean.getLoopCount();
        mLoopPauseInMilliSeconds = alertBean.getLoopPauseInMilliSeconds();
        alertBean.setActive(true);
        alertBean.save();
        List<AlertBean> alertBeans = DataSupport.findAll(AlertBean.class);
        Log.d(TAG, "getData: ---------------" + alertBeans.size());
        mPlayMap = new HashMap<>();
        if (mAssetIds != null && mAssetUrls != null) {
            for (int position = 0; position < mAssetIds.size(); position++) {
                mPlayMap.put(mAssetIds.get(position), mAssetUrls.get(position));
            }
        }
        //  set an custome alarm
        mPlayIds = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = mPlayMap.entrySet();
        Log.d(TAG, "getData: entris " + entries.size());
        if (mAssetPlayOrder != null) {
            for (String assetPlay : mAssetPlayOrder) {
                for (Map.Entry<String, String> playBean: entries) {
                    String playId = playBean.getKey();
                    Log.d(TAG, "getData: playid is " + playId);
                    if (assetPlay.equals(playId)) {
                        mPlayIds.add(playId);
                        Log.d(TAG, "getData: --------------------");
                    }
                }
            }
        }
    }

    private void playAlert(int position) {
//        cancelTask();
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
                if (isStartEvent) {
                    AlexaManager.getInstance(AlertHandleService.this).sendEvent(Event.getAlertStartedEvent(mToken), null);
                }
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isStartEvent = false;
                Log.d(TAG, "onCompletion: LoopCount is " + mLoopCount);
                if (mRealLoopCount < mLoopCount || mLoopCount == 0) {
                    Log.d(TAG, "onCompletion: realLoopCount is " + mRealLoopCount);
                    if (mPlayIds.size() - 1 > mPlayPosition) {
                        mPlayPosition++;
                        Log.d(TAG, "onCompletion: position is " + mPlayPosition);
                        playAlert(mPlayPosition);
                    } else {
//                        Log.d(TAG, "onCompletion: ===============");
                        new CountDownTimer(mLoopPauseInMilliSeconds, SECOND_INTERVAL) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                // TODO Auto-generated method stub
//                                Log.d(TAG, "onTick: --------------------------");
                            }

                            @Override
                            public void onFinish() {
                                Log.d(TAG, "onFinish: start to next loop");
                                if (System.currentTimeMillis() - mStartAlertTime <= LASTING_RING_TIME) {
                                    mRealLoopCount++;
                                    mPlayPosition = 0;
                                    playAlert(mPlayPosition);
                                } else {
                                    // sent stop event and stop
                                    Log.d(TAG, "onFinish: stop to play");
                                    stopPlayer();
                                    //delete alarming alert from db after finishing sounding
                                    //TODO:
                                    DataSupport.delete(AlertBean.class, mId);
                                }
                            }
                        }.start();
                    }
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
    private void stopPlayer() {
        if (mPlayer != null) {
            Log.d(TAG, "stopPlayer: for test");
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            AlexaManager.getInstance(this).sendEvent(Event.getAlertStoppedEvent(mToken), null);
        }
    }
}
