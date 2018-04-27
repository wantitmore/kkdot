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
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.audioplayer.AlexaAudioPlayer;
import com.willblaschko.android.alexa.beans.ListTemplate1Bean;
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
import com.willblaschko.android.alexavoicelibrary.BuildConfig;
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
public class DisplayService extends Service implements BaseListenerFragment.AvsListenerInterface, MusicProgressCallBack {

    private static final String TAG = "DisplayService";
    private static final int AUDIO_RATE = 16000;

    private final static int STATE_LISTENING = 1;
    private final static int STATE_PROCESSING = 2;
    private final static int STATE_SPEAKING = 3;
    private final static int STATE_PROMPTING = 4;
    private final static int STATE_FINISHED = 0;
    private final static int STATE_ERROR = 5;
    private AlexaAudioPlayer audioPlayer;
    private List<AvsItem> avsQueue = new ArrayList<>();
    private MusicProgressCallBack mMusicProgressCallBack;

    private RawAudioRecorder recorder;
    private AlexaManager alexaManager;
    private CircleVoiceStateView mVoiceStateView;
    WindowManager mWindowManager;
    private AlexaReceiver mAlexaReceiver;
    List<View> views = new ArrayList<>();
    private View mView;

    DisplayService service = DisplayService.this;
    private DisplayBinder mBinder = new DisplayBinder();

    public DisplayService() {
        super();
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ---");
        initAlexaAndroid();
        IntentFilter filter = new IntentFilter();
//        filter.addAction("com.konka.android.intent.action.START_VOICE");
        filter.addAction("com.konka.android.intent.action.STOP_VOICE");
        mAlexaReceiver = new AlexaReceiver();
        registerReceiver(mAlexaReceiver, filter);
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
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
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public void onProgressChange(AvsItem item, long offsetInMilliseconds, long duration) {

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
    }

   /* @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }*/

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

            checkQueue();

            //if(BuildConfig.DEBUG)
            if (avsQueue.size() <= 0) {
//                fadeOutView();
                if (mWindowManager != null) {
                    if (mView != null) {
                        mWindowManager.removeViewImmediate(mView);
                        mView = null;
                    }
                   /* if (mVoiceStateView != null) {
                        mWindowManager.removeViewImmediate(mVoiceStateView);
//                        mVoiceStateView = null;
                    }*/
                }
            }
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
            //setState(STATE_PROCESSING); //it only changes the UI
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
            for (int i = response.size() - 1; i >= 0; i--) {
                Log.i(TAG, "AvsItem type:" + response.get(i).getClass().getName());
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

        Log.i(TAG, "Item type " + current.getClass().getName());

        if (current instanceof AvsPlayRemoteItem) {
            //play a URL
            if (!audioPlayer.isPlaying()) {
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
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                switch (state) {
                    case (STATE_LISTENING):
                        stateListening();
                        break;
                    case (STATE_PROCESSING):
                        stateProcessing();
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

    protected void stateListening() {
        Log.d(TAG, "------------> stateListening");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.ACTIVE_LISTENING);
    }

    protected void stateProcessing() {
        Log.d(TAG, "------------> stateProcessing");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.THINKING);
    }

    protected void stateSpeaking() {
        Log.d(TAG, "------------> stateSpeaking");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.SPEAKING);
    }

    protected void stateFinished() {
        Log.d(TAG, "------------> stateFinished");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.IDLE);
        if (mVoiceStateView !=null) {
//            mVoiceStateView.setVisibility(View.INVISIBLE);
        }
    }

    protected void statePrompting() {
        Log.d(TAG, "------------> statePrompting");
    }

    protected void stateNone() {
        Log.d(TAG, "------------> stateNone");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.IDLE);
    }

    protected void  stateError(){
        Log.d(TAG, "------------> stateError");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.SYSTEM_ERR);
    }



    @Override
    public AsyncCallback<AvsResponse, Exception> getRequestCallback() {
        return requestCallback;
    }


    public void startListening() {
        Log.d(TAG, "------------> startListening");
        //recorder is not null,mean that an audio request is sentting
        if (recorder == null) {
            mVoiceStateView.setCurrentState(CircleVoiceStateView.State.LISTENING);
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
                            mVoiceStateView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mVoiceStateView.setCurrentState(CircleVoiceStateView.State.ACTIVE_LISTENING);
                                }
                            });
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
//            Log.i(TAG,"exit writeTo while loop");
            mVoiceStateView.post(new Runnable() {
                @Override
                public void run() {
                    stateProcessing();
                }
            });
            stopListening();
        }

    };

/*    public void fadeOutView() {
        EventBus.getDefault().post("");
        Log.d(TAG, "fadeOutView: --");
//         finish();
        overridePendingTransition(0, R.animator.out_activity);
    }*/

    private void stopListening() {
        Log.d(TAG, "------------> stopListening");
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    protected  void stopCurrentPlayingItem(){
        if (avsQueue.size() == 0|| audioPlayer == null) {
            return;
        }
        AvsItem current = avsQueue.get(0);
        if(audioPlayer.isPlaying()){
            if(current instanceof AvsSpeakItem){
                avsQueue.remove(current);
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

        if (renderObj instanceof PlayerInfoBean) {
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
        }
        if (renderObj instanceof Parcelable) {
            createContentFloatWindow(this, renderObj);
        }
    }

    private void createVoiceStateWindow(Context context) {
        Log.d(TAG, "createVoiceStateWindow: mVoiceStateView " + mVoiceStateView);
        if (mVoiceStateView == null) {
            if (mWindowManager == null) {
                mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            }
            mVoiceStateView = new CircleVoiceStateView(context);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            layoutParams.format = PixelFormat.TRANSLUCENT;
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN;
            layoutParams.alpha = 0.8f;
            layoutParams.gravity = Gravity.END | Gravity.BOTTOM;
            layoutParams.x = 90;
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
        if (avsQueue != null && avsQueue.size() > 0 && avsQueue.get(0) instanceof AvsSpeakItem) {
//            mVoiceStateView.setVisibility(View.INVISIBLE);
        }
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        layoutParams.alpha = 0.8f;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.x = 90;
        layoutParams.y = 0;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        if (mView != null) {
            mWindowManager.removeView(mView);
        }
        ContentView temView = new ContentView();
        mView = temView.setView(this, renderObj);
        mWindowManager.addView(mView, layoutParams);
        if (renderObj instanceof Template1Bean) {
//            mShowingFragment = Template1Fragment.newInstance();
            //add a new float window
        } else if (renderObj instanceof Template2Bean) {
//            mShowingFragment = Template2Fragment.newInstance();

        } else if (renderObj instanceof WeatherTemplateBean) {
//            mShowingFragment = WeatherTemplateFragment.newInstance();
        } else if (renderObj instanceof ListTemplate1Bean) {
//            mShowingFragment = ListTemplate1Fragment.newInstance();
        } else if (renderObj instanceof PlayerInfoBean) {

//            if (mShowingFragment instanceof PlayerInfoFragment) {
//                Log.d(TAG,"just refresh ui,not create new instance");
//                ((PlayerInfoFragment) mShowingFragment).refreshUI((PlayerInfoBean) renderObj);
//                return;
//            }

//            mShowingFragment = PlayerInfoFragment.newInstance();
        } else if (!renderObj.equals("SpeakEnd") && (!renderObj.equals("SpeakStart"))) {
//            mShowingFragment = EmptyFragment.newInstance();
        }

    }


    public class AlexaReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "onReceive: intent is " + intent.getAction() + "--" + ResponseParser.RECOGNIZE_STATE);
            if (Objects.equals(intent.getAction(), "com.konka.android.intent.action.STOP_VOICE")) {
                stopListening();
            }
        }
    }

}
