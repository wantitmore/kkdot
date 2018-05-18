package com.willblaschko.android.alexa.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.beans.AlertBean;
import com.willblaschko.android.alexa.beans.UnSendBean;
import com.willblaschko.android.alexa.data.Event;
import com.willblaschko.android.alexa.utility.AlertUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlertHandlerService extends Service {

    private static final String TAG = "AlertHandlerService";
    private static final long SECOND_INTERVAL = 1000;
    private static final long LASTING_RING_TIME = 60 * 60 * 1000;
    private static final int START_POSITION = 0;
    public static boolean isRinging = false;

    private String mToken;
    private String mType;
    private String mScheduledTime;
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
    public AlertHandlerService.AlertBinder mBinder = new AlertHandlerService.AlertBinder();
    private CountDownTimer mCountDownTimer;
    private boolean isForeground = true;
    private Set<Map.Entry<String, String>> mEntries;
    private String mBackGroundUrl;
    private boolean isPlayingAlert;

    public AlertHandlerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            boolean active = intent.getBooleanExtra("active", false);
            if (active) {//cancelAlertTask
                stopPlayer();
            } else {
                getData(intent);
                mStartAlertTime = System.currentTimeMillis();
                isStartEvent = true;
                playAlert(isForeground, START_POSITION);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public class AlertBinder extends Binder {

        private final AlertHandlerService service;

        private AlertBinder() {
            service = AlertHandlerService.this;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(String speakStatus) {
        if (TextUtils.equals("SpeakStart", speakStatus)) {
            //turn to bg Asset
            Log.d(TAG, "handleEvent: SpeakStart-----------");
            isForeground = false;
            if (mPlayer != null && isPlayingAlert) {
                AlexaManager.getInstance(this).sendEvent(Event.getAlertEnteredBackgroundEvent(mToken), null);
                playAlert(false, 0);
            }
        } else if (TextUtils.equals("SpeakEnd", speakStatus)) {
            Log.d(TAG, "handleEvent: SpeakEnd-----------");
            isForeground = true;
            if (mPlayer != null && isPlayingAlert) {
                AlexaManager.getInstance(this).sendEvent(Event.getAlertEnteredForegroundEvent(mToken), null);
                playAlert(true, 0);
            }
        }
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
        mEntries = mPlayMap.entrySet();
        Log.d(TAG, "getData: entris " + mEntries.size());
        if (mAssetPlayOrder != null) {
            for (String assetPlay : mAssetPlayOrder) {
                for (Map.Entry<String, String> playBean: mEntries) {
                    String playId = playBean.getKey();
                    if (TextUtils.equals(mBackgroundAlertAsset, playId)) {
                        mBackGroundUrl = playBean.getValue();
                    }
                    Log.d(TAG, "getData: playid is " + playId);
                    if (assetPlay.equals(playId)) {
                        mPlayIds.add(playId);
                        Log.d(TAG, "getData: --------------------");
                    } 
                }
            }
        }
    }

    private void playAlert(final boolean isForeground, int position) {
        isPlayingAlert = true;
        if (mPlayer != null) {
            mPlayer.reset();
            mPlayer = null;
        }
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Log.d(TAG, "playAlert: isForeground is " + isForeground + " size is " + mPlayIds.size());
        if (mPlayIds.size() <= 0) {
            setDefaultAsset(isForeground);
        } else if (mPlayIds.size() > 0){
            setForegroundAlert(isForeground, position);
        }
        mPlayer.prepareAsync();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "onPrepared: play start");
                mPlayer.start();
                if (isStartEvent) {
                    if (AlertUtil.isNetworkConnected(AlertHandlerService.this)) {
                        AlexaManager.getInstance(AlertHandlerService.this).sendEvent(Event.getAlertStartedEvent(mToken), null);
                        if (isForeground) {
                            AlexaManager.getInstance(AlertHandlerService.this).sendEvent(Event.getAlertEnteredBackgroundEvent(mToken), null);
                        }
                    } else {
                        UnSendBean unSendBean = new UnSendBean();
                        unSendBean.setToken(mToken);
                        unSendBean.setType("AlertStarted");
                        unSendBean.save();
                    }

                    Intent intent = new Intent();
                    intent.setAction("com.konka.alexa.alertStart");
                    sendBroadcast(intent);
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
                        // TODO Auto-generated method stub
                        mCountDownTimer = new CountDownTimer(mLoopPauseInMilliSeconds, SECOND_INTERVAL) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                // TODO Auto-generated method stub
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

    private void playAlert(int mPlayPosition) {
        playAlert(isForeground, mPlayPosition);
    }

    private void setDefaultAsset(boolean isForeground) {
        AssetFileDescriptor afd = null;
        try {
            if ("TIMER".equals(mType)) {
                Log.d(TAG, "setDefaultAsset: isForeground is " + isForeground);
                if (isForeground) {
                    Log.d(TAG, "setDefaultAsset:TIMER ==========");
                    afd = getAssets().openFd("med_system_alerts_melodic_02" + ".mp3");
                }else {
                    afd = getAssets().openFd("med_system_alerts_melodic_02_short" + ".wav");
                }

            } else if ("ALARM".equals(mType)) {
                if (isForeground) {
                    Log.d(TAG, "setDefaultAsset:ALARM ==========");
                    afd = getAssets().openFd("med_system_alerts_melodic_01" + ".mp3");
                }else {
                    afd = getAssets().openFd("med_system_alerts_melodic_01_short" + ".wav");
                }
            } else if ("REMINDER".equals(mType)) {
                Log.d(TAG, "setDefaultAsset:REMINDER ==========");
                afd = getAssets().openFd("med_alerts_notification_03" + ".mp3");
            }
            if (afd != null) {
                FileDescriptor fd = afd.getFileDescriptor();
                mPlayer.setDataSource(fd, afd.getStartOffset(), afd.getLength());
            }
        } catch (IOException e) {
            Log.e(TAG, "playAlert: error is " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setForegroundAlert(boolean isForeground, int position) {
        String playId;
        String playUrl;
        if (isForeground) {
            playId = mPlayIds.get(position);
            playUrl = mPlayMap.get(playId);
        } else {
            playId = mBackgroundAlertAsset;
            playUrl = mBackGroundUrl;
        }
        try {

            File file = new File(mCacheDir + playId + ".mp3");
            Log.d(TAG, "playAlert: file path is " + file.getAbsolutePath());
            if (file.exists()) {
                Log.d(TAG, "playAlert: local--");
                mPlayer.setDataSource(this, Uri.fromFile(file));
            } else {
                Log.d(TAG, "playAlert: url--");
                if (AlertUtil.isNetworkConnected(this)) {
                    mPlayer.setDataSource(this, Uri.parse(playUrl));
                    downloadFile(playId, playUrl);
//                    downloadFile(mBackgroundAlertAsset, mBackGroundUrl);
                } else {
                    Log.d(TAG, "playAlert: asset---");
                    setDefaultAsset(isForeground);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "playAlert: exception is " + e.getMessage());
            e.printStackTrace();
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
                        Log.e(TAG, "error: download fail- " + e.getMessage());
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();
    }
    private void stopPlayer() {
        isPlayingAlert = false;
        if (mPlayer != null) {
            Log.d(TAG, "stopPlayer: for test");
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (AlertUtil.isNetworkConnected(this)) {
            AlexaManager.getInstance(this).sendEvent(Event.getAlertStoppedEvent(mToken), null);
        } else {
            List<UnSendBean> unSendBeans = DataSupport.where("token = ?", mToken).find(UnSendBean.class);
            if (unSendBeans != null && unSendBeans.size() > 0) {
                UnSendBean unSendBean = unSendBeans.get(0);
                unSendBean.setType("AlertStopped");
                unSendBean.save();
            } else {
                UnSendBean unSendBean = new UnSendBean();
                unSendBean.setType("AlertStopped");
                unSendBean.setToken(mToken);
                unSendBean.save();
            }

        }
        AlertUtil.deleteFile(new File(mCacheDir));
    }
}
