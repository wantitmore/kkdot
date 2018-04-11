package com.willblaschko.android.alexa.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.willblaschko.android.alexa.beans.AlertBean;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by user001 on 2018-4-10.
 */

public class AlertReceiver extends BroadcastReceiver {

    private static final String TAG = "AlertReceiver";
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

    private Context mContext;
    private Map<String, String> mPlayMap;
    private List<String> mPlayIds;
    private MediaPlayer mPlayer;
    private String mCacheDir = Environment.getExternalStorageDirectory() + "/AlexaAudioCache/";
    private long mRealLoopCount = 1;
    private int mPlayPosition;
    private long mStartAlertTime;
    private List<String> mAssetUrls;
    private List<String> mAssetIds;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: -------------------");
        mContext = context;
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            context.startService(new Intent(context, ResetAlertService.class));
        } else {
            getData(intent);
            mStartAlertTime = System.currentTimeMillis();
            playAlert(0);
        }

    }

    private void getData(Intent intent) {
        int id = intent.getIntExtra("id", -1);
        if (id <= 0) {
            return;
        }
        AlertBean alertBean = DataSupport.find(AlertBean.class, id);
        mToken = alertBean.getToken();
        mType = alertBean.getType();
        mScheduledTime = alertBean.getScheduledTime();
//        mAssets = alertBean.getAssets();
        mAssetIds = alertBean.getAssetIds();
        mAssetUrls = alertBean.getAssetUrls();
        mAssetPlayOrder = alertBean.getAssetPlayOrder();
        mBackgroundAlertAsset = alertBean.getBackgroundAlertAsset();
        mLoopCount = /*alertBean.getLoopCount()*/2;
        mLoopPauseInMilliSeconds = alertBean.getLoopPauseInMilliSeconds();
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
        //delete alarming alert from db
        DataSupport.delete(AlertBean.class, id);
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
                mPlayer.setDataSource(mContext, Uri.fromFile(file));
            } else {
                Log.d(TAG, "playAlert: url--");
                downloadFile(playId, playUrl);
                mPlayer.setDataSource(mContext, Uri.parse(playUrl));
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

                if (mRealLoopCount < mLoopCount || mLoopCount == 0) {
                    Log.d(TAG, "onCompletion: realLoopCount is " + mRealLoopCount);
                    if (mPlayIds.size() - 1 > mPlayPosition) {
                        mPlayPosition++;
                        Log.d(TAG, "onCompletion: position is " + mPlayPosition);
                        playAlert(mPlayPosition);
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
                                if (System.currentTimeMillis() - mStartAlertTime <= LASTING_RING_TIME) {
                                    mRealLoopCount++;
                                    mPlayPosition = 0;
                                    playAlert(mPlayPosition);
                                } else {
                                    // sent stop event and stop
                                    Log.d(TAG, "onFinish: stop to play");
                                    stopPlayer();
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
            mPlayer.stop();
            mPlayer = null;
        }
    }
}
