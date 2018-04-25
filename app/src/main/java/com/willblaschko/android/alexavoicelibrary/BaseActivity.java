package com.willblaschko.android.alexavoicelibrary;

import android.app.Instrumentation;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.audioplayer.AlexaAudioPlayer;
import com.willblaschko.android.alexa.callbacks.AsyncCallback;
import com.willblaschko.android.alexa.interfaces.AvsItem;
import com.willblaschko.android.alexa.interfaces.AvsResponse;
import com.willblaschko.android.alexa.interfaces.audioplayer.AvsPlayAudioItem;
import com.willblaschko.android.alexa.interfaces.audioplayer.AvsPlayContentItem;
import com.willblaschko.android.alexa.interfaces.audioplayer.AvsPlayRemoteItem;
import com.willblaschko.android.alexa.interfaces.errors.AvsResponseException;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsMediaNextCommandItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsMediaPauseCommandItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsMediaPlayCommandItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsMediaPreviousCommandItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsReplaceAllItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsReplaceEnqueuedItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsStopItem;
import com.willblaschko.android.alexa.interfaces.speaker.AvsAdjustVolumeItem;
import com.willblaschko.android.alexa.interfaces.speaker.AvsSetMuteItem;
import com.willblaschko.android.alexa.interfaces.speaker.AvsSetVolumeItem;
import com.willblaschko.android.alexa.interfaces.speechrecognizer.AvsExpectSpeechItem;
import com.willblaschko.android.alexa.interfaces.speechsynthesizer.AvsSpeakItem;
import com.willblaschko.android.alexavoicelibrary.actions.BaseListenerFragment;
import com.willblaschko.android.alexavoicelibrary.display.MusicProgressCallBack;

import java.util.ArrayList;
import java.util.List;

import static com.willblaschko.android.alexavoicelibrary.global.Constants.PRODUCT_ID;

/**
 * @author will on 5/30/2016.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseListenerFragment.AvsListenerInterface {

    private final static String TAG = "BaseActivity";

    private final static int STATE_LISTENING = 1;
    private final static int STATE_PROCESSING = 2;
    private final static int STATE_SPEAKING = 3;
    private final static int STATE_PROMPTING = 4;
    private final static int STATE_FINISHED = 0;
    private final static int STATE_ERROR = 5;
    private AlexaManager alexaManager;
    private AlexaAudioPlayer audioPlayer;
    private List<AvsItem> avsQueue = new ArrayList<>();
    private MusicProgressCallBack mMusicProgressCallBack;

    private long startTime = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initAlexaAndroid();
    }

    public void setMusicProgressCallBack(MusicProgressCallBack callBack) {
        mMusicProgressCallBack = callBack;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (audioPlayer != null) {
            audioPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioPlayer != null) {
            //remove callback to avoid memory leaks
            audioPlayer.removeCallback(alexaAudioPlayerCallback);
            audioPlayer.release();
        }
    }


    @Override
    public AsyncCallback<AvsResponse, Exception> getRequestCallback() {
        return requestCallback;
    }


    public abstract void fadeOutView();


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

    //Our callback that deals with removing played items in our media player and then checking to see if more items exist
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
                if(playbackProgressDelayFired ==false && progressReportDelay != 0 &&
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
                if(item instanceof AvsPlayAudioItem||item instanceof AvsPlayRemoteItem) {
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
                fadeOutView();
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

    /**
     * Send an event back to Alexa that we're nearly done with our current playback event, this should supply us with the next item
     * https://developer.amazon.com/public/solutions/alexa/alexa-voice-service/reference/audioplayer#PlaybackNearlyFinished Event
     */
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
            startTime = System.currentTimeMillis();
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(BaseActivity.this)
                            .setTitle("Error")
                            .setMessage(((AvsResponseException) current).getDirective().getPayload().getCode() + ": " + ((AvsResponseException) current).getDirective().getPayload().getDescription())
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            });

            avsQueue.remove(current);
            checkQueue();
        }else{
            //discard the current item and execute the next one
            avsQueue.remove(current);
            checkQueue();
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

    protected abstract void startListening();
    //functions unused
    /*
    private void adjustVolume(long adjust) {
        setVolume(adjust, true);
    }
    private void setVolume(long volume) {
        setVolume(volume, false);
    }
    private void setVolume(final long volume, final boolean adjust) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        final int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        long vol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (adjust) {
            vol += volume * max / 100;
        } else {
            vol = volume * max / 100;
        }
        am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) vol, AudioManager.FLAG_VIBRATE);

        alexaManager.sendVolumeChangedEvent(vol, vol == 0, requestCallback);

        Log.i(TAG, "Volume set to : " + vol + "/" + max + " (" + volume + ")");

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (adjust) {
                    Toast.makeText(BaseActivity.this, "Volume adjusted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BaseActivity.this, "Volume set to: " + (volume / 10), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void setMute(final boolean isMute) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        am.setStreamMute(AudioManager.STREAM_MUSIC, isMute);

        alexaManager.sendMutedEvent(isMute, requestCallback);

        Log.i(TAG, "Mute set to : " + isMute);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, "Volume " + (isMute ? "muted" : "unmuted"), Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    /**
     * Force the device to think that a hardware button has been pressed, this is used for Play/Pause/Previous/Next Media commands
     * @param context
     * @param keyCode keycode for the hardware button we're emulating
     */
    private static void sendMediaButton(Context context, int keyCode) {
        Instrumentation inst = new Instrumentation();
        inst.sendKeyDownUpSync(keyCode);
    }

    private void setState(final int state) {
        runOnUiThread(new Runnable() {
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

    protected abstract void stateListening();

    protected abstract void stateProcessing();

    protected abstract void stateSpeaking();

    protected abstract void stateFinished();

    protected abstract void statePrompting();

    protected abstract void stateNone();

    protected abstract void stateError();

}
