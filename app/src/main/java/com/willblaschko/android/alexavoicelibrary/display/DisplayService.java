package com.willblaschko.android.alexavoicelibrary.display;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.audioplayer.AlexaAudioPlayer;
import com.willblaschko.android.alexa.beans.PlayerInfoBean;
import com.willblaschko.android.alexa.beans.Template1Bean;
import com.willblaschko.android.alexa.beans.Template2Bean;
import com.willblaschko.android.alexa.beans.WeatherTemplateBean;
import com.willblaschko.android.alexa.callbacks.AsyncCallback;
import com.willblaschko.android.alexa.interfaces.AvsItem;
import com.willblaschko.android.alexa.interfaces.AvsResponse;
import com.willblaschko.android.alexa.interfaces.audioplayer.AvsPlayAudioItem;
import com.willblaschko.android.alexa.interfaces.audioplayer.AvsPlayContentItem;
import com.willblaschko.android.alexa.interfaces.audioplayer.AvsPlayRemoteItem;
import com.willblaschko.android.alexa.interfaces.errors.AvsResponseException;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsReplaceAllItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsReplaceEnqueuedItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsStopItem;
import com.willblaschko.android.alexa.interfaces.response.ResponseParser;
import com.willblaschko.android.alexa.interfaces.speechrecognizer.AvsExpectSpeechItem;
import com.willblaschko.android.alexa.interfaces.speechsynthesizer.AvsSpeakItem;
import com.willblaschko.android.alexa.requestbody.DataRequestBody;
import com.willblaschko.android.alexa.service.DownChannelService;
import com.willblaschko.android.alexavoicelibrary.BuildConfig;
import com.willblaschko.android.alexavoicelibrary.R;
import com.willblaschko.android.alexavoicelibrary.actions.BaseListenerFragment;
import com.willblaschko.android.alexavoicelibrary.utility.CommonUtil;
import com.willblaschko.android.alexavoicelibrary.widget.CircleVoiceStateView;
import com.willblaschko.android.alexavoicelibrary.widget.ContentView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ee.ioc.phon.android.speechutils.RawAudioRecorder;
import okio.BufferedSink;

import static com.willblaschko.android.alexavoicelibrary.global.Constants.PRODUCT_ID;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class DisplayService extends Service implements BaseListenerFragment.AvsListenerInterface {

    private static final String TAG = "DisplayService";
    public static final String ALEXA_ALERT_START = "com.konka.alexa.alertStart";
    public static final String ALEXA_ALERT_STOP = "com.konka.alexa.alertStop";
    public static final String ALEXA_BACK = "com.konka.alexa.back";

    private static final int AUDIO_RATE = 16000;
    private final static int STATE_FINISHED = 0;
    private final static int STATE_LISTENING = 1;
    private final static int STATE_ACTIVELISTENING = 2;
    private final static int STATE_THINKING = 3;
    private final static int STATE_SPEAKING = 4;
    private final static int STATE_PROMPTING = 5;
    private final static int STATE_ERROR = 6;

    private AlexaAudioPlayer audioPlayer;
    private List<AvsItem> avsQueue = new ArrayList<>();
    private MusicProgressCallBack mMusicProgressCallBack;
    private Boolean mAcceptEventBusEvent;
    private RawAudioRecorder recorder;
    private AlexaManager alexaManager;
    private CircleVoiceStateView mVoiceStateView;
    WindowManager mWindowManager;
    private AlexaReceiver mAlexaReceiver;
    List<View> views = new ArrayList<>();
    private View mView;
    private Handler runnableHandler;
    DisplayService service = DisplayService.this;
    private DisplayBinder mBinder = new DisplayBinder();
    private boolean isPlayingAlert = false;
    private String templateType;

    public DisplayService() {
        super();
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ---");
        initAlexaAndroid();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.konka.android.intent.action.STOP_VOICE");
        filter.addAction(ALEXA_ALERT_START);
        filter.addAction(ALEXA_ALERT_STOP);
        filter.addAction(ALEXA_BACK);
        mAlexaReceiver = new AlexaReceiver();
        registerReceiver(mAlexaReceiver, filter);
        EventBus.getDefault().register(this);
        loopDownChannel();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mAcceptEventBusEvent = true;
        Log.d(TAG, "onStartCommand: ---");
        boolean playTag = false;
        if (intent != null) {
            playTag = intent.getBooleanExtra("PlayTag", false);
        }
        if (!playTag) {
            alexaManager = AlexaManager.getInstance(this, PRODUCT_ID);
            createVoiceStateWindow(this);
            startListening();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: =================");
        if (mAlexaReceiver != null) {
            unregisterReceiver(mAlexaReceiver);
            mAlexaReceiver = null;
        }
        EventBus.getDefault().unregister(this);
        if (runnableHandler != null) {
            runnableHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ---");
        return mBinder;
    }


    class DisplayBinder extends Binder {

        public void setMusicProgressCallBack(MusicProgressCallBack callBack) {
            service.setMusicProgressCallBack(callBack);
        }

        public void sendPlaybackControllerPreviousCommandIssued() {
            service.sendPlaybackControllerPreviousCommandIssued();
        }

        public void sendPlaybackControllerNextCommandIssued() {
            service.sendPlaybackControllerNextCommandIssued();
        }

        public void sendPlaybackControllerPauseCommandIssued() {
            service.sendPlaybackControllerPauseCommandIssued();
        }

        public void sendPlaybackControllerPlayCommandIssued() {
            service.sendPlaybackControllerPlayCommandIssued();
        }

        public void sendPlaybackStopEvent() {
            DisplayService.this.sendPlaybackStopEvent();
        }

        public void stop() {
            if (audioPlayer != null) {
                audioPlayer.stop();
            }
        }

        public void release() {
            if (audioPlayer != null) {
                //remove callback to avoid memory leaks
                audioPlayer.removeCallback(alexaAudioPlayerCallback);
                audioPlayer.release();
            }
        }

        public void stopService() {
            stopSelf();
        }

        public void setAcceptEventBusEvent(Boolean flag){
            mAcceptEventBusEvent = flag;
        }

    }


    private void loopDownChannel () {
        runnableHandler = new Handler(Looper.getMainLooper());
        runnableHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: reset downchannel");
                runnableHandler.removeCallbacks(this);
                stopService(new Intent(DisplayService.this, DownChannelService.class));
                startService(new Intent(DisplayService.this, DownChannelService.class));
                runnableHandler.postDelayed(this, 10 * 60 * 1000);
            }
        }, 10 * 60 * 1000);
    }

    public void setMusicProgressCallBack(MusicProgressCallBack callBack) {
        mMusicProgressCallBack = callBack;
    }


    private void initAlexaAndroid() {
        //get our AlexaManager instance for convenience
        alexaManager = AlexaManager.getInstance(this, PRODUCT_ID);

        //instantiate our audio player
        audioPlayer = AlexaAudioPlayer.getInstance(this);

        //Remove the current item and check for more items once we've finished playing
        audioPlayer.addCallback(alexaAudioPlayerCallback);

        //open our downchannel
        //alexaManager.sendOpenDownchannelDirective(requestCallback);


        //synchronize our device
        //alexaManager.sendSynchronizeStateEvent(requestCallback);
    }

    private AlexaAudioPlayer.Callback alexaAudioPlayerCallback = new AlexaAudioPlayer.Callback() {

        private boolean almostDoneFired = false;
        private boolean playbackStartedFired = false;
        private boolean playbackProgressDelayFired = false;
        long lastInterval = 0;

        @Override
        public void playerPrepared(AvsItem pendingItem) {

        }

        @Override
        public void playerProgress(AvsItem item, long offsetInMilliseconds, long  duration) {
            float percent;
            if(duration > 0){
                percent = offsetInMilliseconds/duration;
            }else{
                percent = 0;
            }
            if (BuildConfig.DEBUG) {
                //Log.i(TAG, "Player percent: " + percent);
            }
            if (item instanceof AvsPlayContentItem || item == null) {
                return;
            }

            if(item instanceof AvsPlayRemoteItem){
                AvsPlayRemoteItem playRemoteItem = (AvsPlayRemoteItem)item;
                long progressReportDelay = playRemoteItem.getProgressReportDelayInMilliseconds();
                long progressReportInterval = playRemoteItem.getmProgressReportIntervalInMilliseconds();
                if(!playbackProgressDelayFired && progressReportDelay != 0 &&
                        (offsetInMilliseconds >= progressReportDelay && offsetInMilliseconds - progressReportDelay < 1000)){
                    playbackProgressDelayFired = true;
                    sendPlaybackProgressReportDelayElapsedEvent(playRemoteItem.getToken(),offsetInMilliseconds);
                }

                if(progressReportInterval != 0 && offsetInMilliseconds/progressReportInterval != lastInterval ){

                    if(offsetInMilliseconds/progressReportInterval - lastInterval == 1){
                        sendPlaybackProgressReportIntervalElapsedEvent(playRemoteItem.getToken(),offsetInMilliseconds);
                    }
                    lastInterval = offsetInMilliseconds/progressReportInterval;
                }
            }

            if (mMusicProgressCallBack != null) {
                mMusicProgressCallBack.onProgressChange(item, offsetInMilliseconds, duration);
            }

            if (!playbackStartedFired) {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "PlaybackStarted " + item.getToken() + " fired: " + percent);
                }
                playbackStartedFired = true;
                sendPlaybackStartedEvent(item);
            }
            if (!almostDoneFired && percent > .8f) {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "AlmostDone " + item.getToken() + " fired: " + percent);
                }
                almostDoneFired = true;
                if(item instanceof AvsPlayAudioItem ||item instanceof AvsPlayRemoteItem) {
                    Log.i(TAG, "AlmostDone " + item.getToken() + " fired: " + percent);
                    sendPlaybackNearlyFinishedEvent( item, offsetInMilliseconds);
                }
            }
        }

        @Override
        public void playerStop(){
            almostDoneFired = false;
            playbackStartedFired = false;
            playbackProgressDelayFired =false;
            lastInterval = 0;
        }
        @Override
        public void itemComplete(AvsItem completedItem) {
            almostDoneFired = false;
            playbackStartedFired = false;
            playbackProgressDelayFired =false;
            lastInterval = 0;

            if (completedItem instanceof AvsPlayContentItem || completedItem == null) {
                return;
            }
            avsQueue.remove(completedItem);
            if(completedItem instanceof AvsSpeakItem){
                setState(STATE_FINISHED);
            }

            {
                Log.i(TAG, "Complete " + completedItem.getToken() + " fired");
            }
            sendPlaybackFinishedEvent(completedItem);
            removeContentView();
            checkQueue();


        }

        @Override
        public boolean playerError(AvsItem item, int what, int extra) {
            Log.i(TAG,item==null?"no item":item.getToken()+" what:"+ what+",extra:"+extra);
            return false;
        }

        @Override
        public void dataError(AvsItem item, Exception e) {
            e.printStackTrace();
        }


    };

    private void removeContentView() {
        if (mWindowManager != null && mView != null) {
            Log.d(TAG, "removeContentView: --->" + templateType);
            if ("listTemType".equals(templateType)) {
                Log.d(TAG, "removeContentView: -->" + ", " + avsQueue.size());
                if (mResponseItems.size() > 0 && mResponseItems.get(0).equals("AvsExpectSpeechItem")) {
                    return;
                }
            }
            Log.d(TAG, "itemComplete: removeContentView " + Thread.currentThread().getName());
            mWindowManager.removeViewImmediate(mView);
            mView = null;
        }
    }

    private void sendPlaybackNearlyFinishedEvent(AvsItem item, long offsetInMilliseconds){
        if (item != null) {
            alexaManager.sendPlaybackNearlyFinishedEvent(item, offsetInMilliseconds, requestCallback);
            Log.i(TAG, "Sending PlaybackNearlyFinishedEvent");
        }
    }

    public void sendPlaybackControllerPauseCommandIssued() {
        alexaManager.sendPlaybackControllerPauseCommandIssued(requestCallback);
    }

    public void sendPlaybackControllerPlayCommandIssued() {
        alexaManager.sendPlaybackControllerPlayCommandIssued(requestCallback);
    }

    public void sendPlaybackControllerPreviousCommandIssued() {
        alexaManager.sendPlaybackControllerPreviousCommandIssued(requestCallback);
    }

    public void sendPlaybackControllerNextCommandIssued() {
        alexaManager.sendPlaybackControllerNextCommandIssued(requestCallback);
    }

    /**
     * Send an event back to Alexa that we're starting a speech event
     * https://developer.amazon.com/public/solutions/alexa/alexa-voice-service/reference/audioplayer#PlaybackNearlyFinished Event
     */
    private void sendPlaybackStartedEvent(AvsItem item) {
        alexaManager.sendPlaybackStartedEvent(item, 0, null);
        Log.i(TAG, "Sending sendPlaybackStartedEvent");
    }


    private void sendPlaybackProgressReportDelayElapsedEvent(String token,long offset){
        alexaManager.sendPlaybackProgressReportDelayElapsedEvent(token,offset,requestCallback);
        Log.i(TAG, "Sending sendPlaybackProgressReportDelayElapsedEvent");
    }

    private void sendPlaybackProgressReportIntervalElapsedEvent(String token,long offset){
        alexaManager.sendPlaybackProgressReportIntervalElapsedEvent(token,offset,requestCallback);
        Log.i(TAG, "Sending sendPlaybackProgressReportIntervalElapsedEvent");
    }

    private void sendPlaybackStopEvent() {
        AvsPlayRemoteItem playRemoteItem = audioPlayer.getLastAvsPlayRemoteItem();
        if(playRemoteItem != null) {
            alexaManager.sendPlaybackStopEvent(playRemoteItem.getToken(),playRemoteItem.getStartOffset(),null);
            Log.i(TAG, "Sending sendPlaybackStopEvent");
        }
    }

    private void sendPlaybackPausedEvent(){
        AvsPlayRemoteItem playRemoteItem = audioPlayer.getLastAvsPlayRemoteItem();
        if(playRemoteItem != null) {
            alexaManager.sendPlaybackPausedEvent(playRemoteItem.getToken(), playRemoteItem.getStartOffset(), null);
            Log.i(TAG, "Sending sendPlaybackPausedEvent");
        }
    }

    private void sendPlaybackResumedEvent(){
        AvsPlayRemoteItem playRemoteItem = audioPlayer.getLastAvsPlayRemoteItem();
        if(playRemoteItem != null) {
            alexaManager.sendPlaybackResumedEvent(playRemoteItem.getToken(), playRemoteItem.getStartOffset(), null);
            Log.i(TAG, "Sending sendPlaybackResumedEvent");
        }
    }

    /**
     * Send an event back to Alexa that we're done with our current speech event, this should supply us with the next item
     * https://developer.amazon.com/public/solutions/alexa/alexa-voice-service/reference/audioplayer#PlaybackNearlyFinished Event
     */
    private void sendPlaybackFinishedEvent(AvsItem item) {
        if (item != null) {
            alexaManager.sendPlaybackFinishedEvent(item, null);
            Log.i(TAG, "Sending PlaybackFinishedEvent");
        }
    }

    //async callback for commands sent to Alexa Voice
    private AsyncCallback<AvsResponse, Exception> requestCallback = new AsyncCallback<AvsResponse, Exception>() {
        @Override
        public void start() {
            Log.i(TAG, "Event Start");
        }

        @Override
        public void success(AvsResponse result) {
            Log.i(TAG, "Event Success");
            handleResponse(result);
        }

        @Override
        public void failure(Exception error) {
            error.printStackTrace();
            Log.i(TAG, "Event Error");
            setState(STATE_ERROR);
        }

        @Override
        public void complete() {
            Log.i(TAG, "Event Complete");
        }
    };

    private List<String> mResponseItems = new ArrayList<>();

    /**
     * Handle the response sent back from Alexa's parsing of the Intent, these can be any of the AvsItem types (play, speak, stop, clear, listen)
     * @param response a List<AvsItem> returned from the mAlexaManager.sendTextRequest() call in sendVoiceToAlexa()
     */
    private void handleResponse(AvsResponse response) {
        boolean checkAfter = true;
        boolean addTotail = true;
        if (response != null) {
            //if we have a clear queue item in the list, we need to clear the current queue before proceeding
            //iterate backwards to avoid changing our array positions and getting all the nasty errors that come
            //from doing that
            setState(STATE_FINISHED);
            Log.i(TAG,"response.size():"+response.size());
            mResponseItems.clear();
            for (int i = response.size() - 1; i >= 0; i--) {
                Log.i(TAG, "AvsItem type:" + response.get(i).getClass().getName());
                mResponseItems.add(response.get(i).getClass().getSimpleName());
                if(response.get(i) instanceof AvsReplaceAllItem){
                    avsQueue.clear();
                    if(audioPlayer.isPlaying()) {
                        audioPlayer.stop();
                    }
                    response.remove(i);
                }else if(response.get(i) instanceof AvsReplaceEnqueuedItem){
                    avsQueue.clear();
                    checkAfter = false;
                    response.remove(i);
                }else if(response.get(i) instanceof AvsStopItem){
                    addTotail = false;
                    avsQueue.remove(audioPlayer.getCurrentItem());
                }else if(response.get(i) instanceof  AvsSpeakItem) {
                    addTotail = false;
                    if(audioPlayer.isPlaying()) {
                        audioPlayer.stop();
                    }
                }
            }
            Log.i(TAG, "Adding " + response.size() + " items to our queue");
            if (response.size() <= 0 && mView != null) {
                Log.d(TAG, "handleResponse: removeContentView");
                mView.post(new Runnable() {
                    @Override
                    public void run() {
                        removeContentView();
                    }
                });
            }
            if (BuildConfig.DEBUG) {
                for (int i = 0; i < response.size(); i++) {
                    Log.i(TAG, "\tAdding: " + response.get(i).getToken());
                }
            }
            if(addTotail) {
                avsQueue.addAll(response);
            }else{
                avsQueue.addAll(0,response);
            }
            Log.i(TAG,"avsQueue member:");
            for(int i = 0;i<avsQueue.size();i++){
                Log.i(TAG, i+":"+avsQueue.get(i).getClass().getName());
            }
        }
        if(checkAfter) {
            checkQueue();
        }
    }


    /**
     * Check our current queue of items, and if we have more to parse (once we've reached a play or listen callback) then proceed to the
     * next item in our list.
     *
     * We're handling the AvsReplaceAllItem in handleResponse() because it needs to clear everything currently in the queue, before
     * the new items are added to the list, it should have no function here.
     */
    private void checkQueue() {

        //if we're out of things, hang up the phone and move on
        if (avsQueue.size() == 0) {
            return;
        }

        final AvsItem current = avsQueue.get(0);
        for (int i = 0; i < avsQueue.size(); i++) {
            Log.d(TAG, "checkQueue:  Item type " + avsQueue.get(i).getClass().getName());
        }

        Log.i(TAG, "Item type " + current.getClass().getName());

        if (current instanceof AvsPlayRemoteItem) {
            //play a URL
            if (!audioPlayer.isPlaying() && !isPlayingAlert) {
                if(((AvsPlayRemoteItem) current).isNeedResumed()) {
                    sendPlaybackResumedEvent();
                }
                audioPlayer.playItem((AvsPlayRemoteItem) current);
            }
        } else if (current instanceof AvsPlayContentItem) {
            //play a URL
            if (!audioPlayer.isPlaying()) {
                audioPlayer.playItem((AvsPlayContentItem) current);
            }
        } else if (current instanceof AvsSpeakItem) {
            //play a sound file
            if (!audioPlayer.isPlaying()) {
                audioPlayer.playItem((AvsSpeakItem) current);
            }
            setState(STATE_SPEAKING);
        } else if (current instanceof AvsStopItem) {
            //stop our play
            Log.d(TAG,"audio player stop");
            audioPlayer.stop();
            sendPlaybackStopEvent();
            if (mMusicProgressCallBack != null) {
                mMusicProgressCallBack.onPlaystateChange();
            }
            avsQueue.remove(current);
        } else if (current instanceof AvsExpectSpeechItem) {
            startListening();
            avsQueue.remove(current);
        }
        else if (current instanceof AvsResponseException) {
            /*DisplayService.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(BaseActivity.this)
                            .setTitle("Error")
                            .setMessage(((AvsResponseException) current).getDirective().getPayload().getCode() + ": " + ((AvsResponseException) current).getDirective().getPayload().getDescription())
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            });*/
            String errorMsg = ((AvsResponseException) current).getDirective().getPayload().getCode() + ": " + ((AvsResponseException) current).getDirective().getPayload().getDescription();
            Log.e(TAG, "error occur AvsResponseException-->" + errorMsg);
            removeContentView();
            avsQueue.remove(current);
            checkQueue();
        }else{
            //discard the current item and execute the next one
            avsQueue.remove(current);
            checkQueue();
        }
    }


    private void setState(final int state) {
        Log.d(TAG, "setState: -------------- " + state);
        if (mVoiceStateView != null) {
            mVoiceStateView.post(new Runnable() {
                @Override
                public void run() {
                    switch (state) {
                        case (STATE_LISTENING):
                            stateListening();
                            break;
                        case (STATE_ACTIVELISTENING):
                            stateActivelistening();
                            break;
                        case (STATE_THINKING):
                            stateThinking();
                            break;
                        case (STATE_SPEAKING):
                            stateSpeaking();
                            break;
                        case (STATE_FINISHED):
                            stateFinished();
                            break;
                        case (STATE_PROMPTING):
                            statePrompting();
                            break;
                        case (STATE_ERROR):
                            stateError();
                            break;
                        default:
                            stateNone();
                            break;
                    }
                }
            });
        }
    }

    protected void stateListening(){
        Log.d(TAG, "------------> stateListening");
        if (mVoiceStateView != null) {
            mVoiceStateView.setCurrentState(CircleVoiceStateView.State.LISTENING);
        }
    }

    protected void stateActivelistening() {
        Log.d(TAG, "------------> stateActivelistening");
        if (mVoiceStateView != null) {
            mVoiceStateView.setCurrentState(CircleVoiceStateView.State.ACTIVE_LISTENING);
        }
    }

    protected void stateThinking() {
        Log.d(TAG, "------------> stateThinking");
        if (mVoiceStateView != null) {
            mVoiceStateView.setCurrentState(CircleVoiceStateView.State.THINKING);
        }
    }

    protected void stateSpeaking() {
        Log.d(TAG, "------------> stateSpeaking");
        if (mVoiceStateView != null) {
            mVoiceStateView.setCurrentState(CircleVoiceStateView.State.SPEAKING);
        }
    }

    protected void stateFinished() {
        Log.d(TAG, "------------> stateFinished");
        if (mVoiceStateView !=null) {
            mVoiceStateView.setCurrentState(CircleVoiceStateView.State.IDLE);
        }
    }

    protected void statePrompting() {
        Log.d(TAG, "------------> statePrompting");
    }

    protected void stateNone() {
        Log.d(TAG, "------------> stateNone");
        if (mVoiceStateView != null) {
            mVoiceStateView.setCurrentState(CircleVoiceStateView.State.IDLE);
        }
    }

    protected void  stateError(){
        Log.d(TAG, "------------> stateError");
        if (mVoiceStateView != null) {
            mVoiceStateView.setCurrentState(CircleVoiceStateView.State.SYSTEM_ERR);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1500);
                mVoiceStateView.setVisibility(View.GONE);
            }
        }).start();
    }



    @Override
    public AsyncCallback<AvsResponse, Exception> getRequestCallback() {
        return requestCallback;
    }


    public void startListening() {
        Log.d(TAG, "------------> startListening");
        //recorder is not null,mean that an audio request is sentting
        if (mVoiceStateView != null) {
            mVoiceStateView.setVisibility(View.VISIBLE);
        }
        if (recorder == null) {
            setState(STATE_LISTENING);
            Log.d(TAG, "startListening: recorder = null");
            recorder = new RawAudioRecorder(AUDIO_RATE);
            recorder.start();
            alexaManager.sendAudioRequest(requestBody, getRequestCallback());
            stopCurrentPlayingItem();
        }
    }


    private DataRequestBody requestBody = new DataRequestBody() {
        @Override
        public void writeTo(BufferedSink sink) throws IOException {
//            Log.d(TAG, "writeTo: recorder is " + recorder + ", is Pa");
            boolean changeState = true;
//            Log.i(TAG,"will enter writeTo while loop");
            while (recorder != null && !recorder.isPausing()) {
                try {
//                    Log.d(TAG, "writeTo: record is null? " + recorder);
                    if (recorder != null) {
                        final float rmsdb = recorder.getRmsdb();
//                        Log.d(TAG, "run: ----rmsdb is " + rmsdb + ",recorder.isPausing():" + recorder.isPausing());
                        CircleVoiceStateView.State currentState = mVoiceStateView.getCurrentState();

                        if (changeState && rmsdb != 0) {
                           setState(STATE_ACTIVELISTENING);
                            changeState =false;
                        }
                    }

                    if (sink != null && recorder != null) {
                        sink.write(recorder.consumeRecording());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "writeTo: error is " + e.getMessage());
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            setState(STATE_THINKING);
            stopListening();
        }

    };

    private void stopListening() {
        Log.d(TAG, "------------> stopListening");
        if (recorder != null) {
            recorder.stop();
            if (recorder != null) {
                recorder.release();
            }
            recorder = null;
        }
    }


    protected void stopCurrentPlayingItem(){
        if (avsQueue.size() == 0 || audioPlayer == null) {
            return;
        }
        AvsItem current = avsQueue.get(0);
        if(audioPlayer.isPlaying()){
            if(current instanceof AvsSpeakItem){
                if (TextUtils.equals(templateType, "listTemType")) {//在有音乐播放等情况下失效，需将 一组一组的删除
                    int lastItemIndex = avsQueue.size() - 1;
                    if (avsQueue.get(lastItemIndex) instanceof AvsExpectSpeechItem && avsQueue.get(lastItemIndex - 1) instanceof AvsSpeakItem) {
                        Log.d(TAG, "stopCurrentPlayingItem: avsQueue.size() is " + avsQueue.size() + ", avsQueue.get(lastItemIndex) " + avsQueue.get(lastItemIndex).getClass().getSimpleName()
                         + ", avsQueue.get(lastItemIndex - 1) is " + avsQueue.get(lastItemIndex - 1));
                        Log.d(TAG, "--stopCurrentPlayingItem: avsQueue.size() is " + avsQueue.size());
                        for (int i = 0; i < 3; i++) {
                            avsQueue.remove(avsQueue.size() - 1);
                        }
                    }
                }else {
                    avsQueue.remove(current);
                }
            }

            if(audioPlayer.getCurrentItem() instanceof AvsPlayRemoteItem){
                ((AvsPlayRemoteItem) audioPlayer.getCurrentItem()).setStartoffset(audioPlayer.getCurrentPosition());
                ((AvsPlayRemoteItem) audioPlayer.getCurrentItem()).SetNeedResumed(true);
                sendPlaybackPausedEvent();
            }

            audioPlayer.stop(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(Object renderObj) {
        Log.d(TAG, "handleEvent: =======");
        if ((renderObj instanceof PlayerInfoBean) && mAcceptEventBusEvent) {
            String topActivity = CommonUtil.getTopActivity(this);
            Log.d(TAG, "handleEvent: topActivity is " +  topActivity);
            if ("com.willblaschko.android.alexavoicelibrary.display.PlayInfoActivity".equals(topActivity)) {
                Log.d(TAG,"just refresh ui,not create new instance");
                return;
            }
            Intent intent = new Intent(this, PlayInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("args", (Parcelable) renderObj);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        } else if (renderObj instanceof String && TextUtils.equals((String)renderObj, "Controls-Only")) {
            Intent intent = new Intent(this, PlayInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (renderObj instanceof AvsResponse) {
            handleResponse((AvsResponse) renderObj);
        }
        if (renderObj instanceof Parcelable) {
            createContentFloatWindow(this, renderObj);
        }
    }

    private void createVoiceStateWindow(Context context) {
        Log.d(TAG, "createVoiceStateWindow: mVoiceStateView " + mVoiceStateView);
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if(mVoiceStateView == null) {
            mVoiceStateView = new CircleVoiceStateView(context);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
            layoutParams.format = PixelFormat.TRANSLUCENT;
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN;
            layoutParams.alpha = 0.8f;
            layoutParams.gravity = Gravity.END | Gravity.BOTTOM;
            layoutParams.x = 60;
            layoutParams.y = 0;
            layoutParams.width = 220;
            layoutParams.height = 220;

            mWindowManager.addView(mVoiceStateView, layoutParams);
        } else {
            mVoiceStateView.setVisibility(View.VISIBLE);
        }
    }



    private void createContentFloatWindow(Context context, Object  renderObj) {
        if (renderObj == null) {
            return;
        }
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        layoutParams.alpha = 0.8f;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.x = 90;
        layoutParams.y = 0;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.windowAnimations = android.R.style.Animation_Translucent;
        if (mView != null) {
            mWindowManager.removeView(mView);
            mView = null;
        }
        if (mVoiceStateView != null/* && audioPlayer != null && !(audioPlayer.getCurrentItem() instanceof AvsExpectSpeechItem)*/) {
            mVoiceStateView.setVisibility(View.GONE);
        }
        ContentView temView = new ContentView();
        mView = temView.setView(this, renderObj);
        if (renderObj instanceof Template1Bean || renderObj instanceof Template2Bean) {
            templateType = "normalType";
            TextView textView = (TextView) mView.findViewById(R.id.text_content);
            textView.setOnKeyListener(new View.OnKeyListener() {//listen backkey
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    Log.d(TAG, "onKey: keycode is " + keyCode);
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        removeContentView();
                        if (audioPlayer != null && audioPlayer.isPlaying()) {
                            audioPlayer.stop(false);
                            avsQueue.remove(audioPlayer.getCurrentItem());
                            checkQueue();
                        }
                    }
                    return false;
                }
            });
        } else if(renderObj instanceof WeatherTemplateBean) {
            templateType = "weatherType";
            mView.setFocusableInTouchMode(true);
            mView.setOnKeyListener(new View.OnKeyListener() {//listen backkey
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    Log.d(TAG, "onKey: keycode is " + keyCode);
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        removeContentView();
                        if (audioPlayer != null && audioPlayer.isPlaying()) {
                            audioPlayer.stop(false);
                            avsQueue.remove(audioPlayer.getCurrentItem());
                            checkQueue();
                        }
                    }
                    return false;
                }
            });
        } else {
            templateType = "listTemType";
        }
        mWindowManager.addView(mView, layoutParams);
    }


    public class AlexaReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.d(TAG, "onReceive: intent is " + action + "--" + ResponseParser.RECOGNIZE_STATE);
            if (Objects.equals(action, "com.konka.android.intent.action.STOP_VOICE")) {
                stopListening();
            } else if (Objects.equals(action, ALEXA_ALERT_START)) {
                //pause music if it's playing
                isPlayingAlert = true;
                if (avsQueue.size() == 0|| audioPlayer == null) {
                    return;
                }
                if(audioPlayer.isPlaying()){
                    if(audioPlayer.getCurrentItem() instanceof AvsPlayRemoteItem){
                        ((AvsPlayRemoteItem) audioPlayer.getCurrentItem()).SetNeedResumed(true);
                        sendPlaybackPausedEvent();
                        audioPlayer.pause();
                    }
                }
            } else if (Objects.equals(action, ALEXA_ALERT_STOP)) {
                isPlayingAlert = false;
                checkQueue();
            } else if (Objects.equals(ALEXA_BACK, action)) {
                mWindowManager.removeViewImmediate(mView);
                mView = null;
                Log.d(TAG, "onReceive: -------------------------------- " + audioPlayer.isPlaying());
                if (audioPlayer != null && audioPlayer.isPlaying()) {
//                    audioPlayer.stop(false);
//                    avsQueue.remove(audioPlayer.getCurrentItem());
                    stopCurrentPlayingItem();
                    checkQueue();
                }
            }
        }
    }

}
