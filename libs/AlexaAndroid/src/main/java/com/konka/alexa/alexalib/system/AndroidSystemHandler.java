package com.konka.alexa.alexalib.system;

import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;

import com.konka.alexa.alexalib.AlexaManager;
import com.konka.alexa.alexalib.beans.AlertBean;
import com.konka.alexa.alexalib.interfaces.AvsItem;
import com.konka.alexa.alexalib.interfaces.AvsResponse;
import com.konka.alexa.alexalib.interfaces.speaker.AvsAdjustVolumeItem;
import com.konka.alexa.alexalib.interfaces.speaker.AvsSetMuteItem;
import com.konka.alexa.alexalib.interfaces.speaker.AvsSetVolumeItem;
import com.konka.alexa.alexalib.receiver.AlertHandlerReceiver;
import com.konka.alexa.alexalib.service.DownChannelService;
import com.konka.alexa.alexalib.data.Directive;
import com.konka.alexa.alexalib.data.Event;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by will on 4/8/2017.
 */

public class AndroidSystemHandler {
    private static final String TAG = "AndroidSystemHandler";
    private static AndroidSystemHandler instance;
    private Context context;
    private AndroidSystemHandler(Context context){
        this.context = context.getApplicationContext();
    }
    public static AndroidSystemHandler getInstance(Context context){
        if(instance == null){
            instance = new AndroidSystemHandler(context);
        }
        return instance;
    }
    public void handleItems(@NonNull AvsResponse response, boolean fromDowmChannel){
        for(AvsItem current: response){

            Log.i(TAG, "Handling AvsItem: " + current.getClass());
            if(current instanceof com.konka.alexa.alexalib.interfaces.system.AvsSetEndpointItem){
                Log.i(TAG, "Setting URL endpoint: " + ((com.konka.alexa.alexalib.interfaces.system.AvsSetEndpointItem) current).getEndpoint());
                AlexaManager.getInstance(context)
                        .setUrlEndpoint(((com.konka.alexa.alexalib.interfaces.system.AvsSetEndpointItem) current).getEndpoint());

                context.stopService(new Intent(context, DownChannelService.class));
                context.startService(new Intent(context, DownChannelService.class));
            }else if (current instanceof AvsSetVolumeItem) {
                //set our volume
                setVolume(((AvsSetVolumeItem) current).getVolume());
            } else if(current instanceof AvsAdjustVolumeItem){
                //adjust the volume
                adjustVolume(((AvsAdjustVolumeItem) current).getAdjustment());
            } else if(current instanceof AvsSetMuteItem){
                //mute/unmute the device
                setMute(((AvsSetMuteItem) current).isMute());
            }else if(current instanceof com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsMediaPlayCommandItem){
                //fake a hardware "play" press
                sendMediaButton(KeyEvent.KEYCODE_MEDIA_PLAY);
                Log.i(TAG, "Media play command issued");
            }else if(current instanceof com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsMediaPauseCommandItem){
                //fake a hardware "pause" press
                sendMediaButton(KeyEvent.KEYCODE_MEDIA_PAUSE);
                Log.i(TAG, "Media pause command issued");
            }else if(current instanceof com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsMediaNextCommandItem){
                //fake a hardware "next" press
                sendMediaButton(KeyEvent.KEYCODE_MEDIA_NEXT);
                Log.i(TAG, "Media next command issued");
            }else if(current instanceof com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsMediaPreviousCommandItem){
                //fake a hardware "previous" press
                sendMediaButton(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
                Log.i(TAG, "Media previous command issued");
            }else if (current instanceof com.konka.alexa.alexalib.interfaces.alerts.AvsSetAlertItem){
                Log.d(TAG, "handleItems: current item is " + ((com.konka.alexa.alexalib.interfaces.alerts.AvsSetAlertItem) current).getType() + "--");
                setAlarm((com.konka.alexa.alexalib.interfaces.alerts.AvsSetAlertItem) current);
            }else if (current instanceof com.konka.alexa.alexalib.interfaces.alerts.AvsDeleteAlertItem){
                Log.d(TAG, "handleItems: ---deleteAlert");
                deleteAlert(current);
            } else if (current instanceof com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsStopItem && fromDowmChannel) {
                Log.d(TAG, "handleItems: stop music");
                mOnAudioPlayerListener.stop();
            }
        }
    }

    private void deleteAlert(AvsItem deleteAlertItem) {
        String token = deleteAlertItem.getToken();
        Log.d(TAG, "deleteAlert: token is " + token);
        List<AlertBean> alertBeans = DataSupport.where("token = ?", token).find(AlertBean.class);
        Log.d(TAG, "deleteAlert: ---------" + alertBeans);
        if (alertBeans != null && alertBeans.size() > 0) {
            int id = alertBeans.get(0).getId();
            Log.d(TAG, "deleteAlert: id is " + id);
            Intent intent = new Intent(context, AlertHandlerReceiver.class);
            intent.putExtra("tag", "deleteAlert");
            intent.putExtra("id", id);
            intent.putExtra("token", token);
            context.sendBroadcast(intent);
        }
    }


    public AvsResponse handleDirective(Directive directive){
        try {
            AvsResponse response = new AvsResponse();
            AvsItem item = com.konka.alexa.alexalib.interfaces.response.ResponseParser.parseDirective(directive);
            if(directive.isPlayBehaviorReplaceAll()){
                response.add(0, new com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsReplaceAllItem(directive.getPayload().getToken()));
            }
            if(directive.isPlayBehaviorReplaceEnqueued()){
                response.add(new com.konka.alexa.alexalib.interfaces.playbackcontrol.AvsReplaceEnqueuedItem(directive.getPayload().getToken()));
            }
            if(item == null){
                return null;
            }
            response.add(item);
            handleItems(response, true);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getVolume(){
        AudioManager am = (AudioManager) context.getSystemService(AUDIO_SERVICE);;
        long vol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        return vol;
    }
    @TargetApi(Build.VERSION_CODES.M)
    public boolean isMute(){
        AudioManager am = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        boolean mute = am.isStreamMute(AudioManager.STREAM_MUSIC);
        return mute;
    }

    private void setAlarm(com.konka.alexa.alexalib.interfaces.alerts.AvsSetAlertItem item){
        Log.d(TAG, "setAlarm: ==========");

        try {
            Intent intent = new Intent(context, AlertHandlerReceiver.class);

//        intent.put("alertItem", item);
            Bundle bundle = new Bundle();
            bundle.putString("type", item.getType());
            bundle.putString("scheduledTime", item.getScheduledTime());
            bundle.putSerializable("assets", (Serializable) item.getAssets());
            bundle.putStringArrayList("assetPlayOrder", (ArrayList<String>) item.getAssetPlayOrder());
            bundle.putLong("loopPauseInMilliSeconds", item.getLoopPauseInMilliSeconds());
            bundle.putLong("loopCount", item.getLoopCount());
            bundle.putString("backgroundAlertAsset", item.getBackgroundAlertAsset());
            bundle.putString("token", item.getToken());
            intent.putExtras(bundle);

            context.sendBroadcast(intent);
//        context.startService(intent);
//        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
            AlexaManager.getInstance(context)
                    .sendEvent(Event.getSetAlertSucceededEvent(item.getToken()), null);
        } catch (Exception e) {
            AlexaManager.getInstance(context).sendEvent(Event.getSetAlertFailedEvent(item.getToken()), null);
            e.printStackTrace();
        }
    }


    /**
     * Force the device to think that a hardware button has been pressed, this is used for Play/Pause/Previous/Next Media commands
     * @param keyCode keycode for the hardware button we're emulating
     */
    private static void sendMediaButton(int keyCode) {
        Instrumentation inst = new Instrumentation();
        inst.sendKeyDownUpSync(keyCode);
    }

    private void adjustVolume(long adjust){
        setVolume(adjust, true);
    }
    private void setVolume(long volume){
        setVolume(volume, false);
    }
    private void setVolume(final long volume, final boolean adjust){

        AudioManager am = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        final int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        long vol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (adjust) {
            vol += volume * max / 100;
        } else {
            vol = volume * max / 100;
        }
        am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) vol, AudioManager.FLAG_VIBRATE|AudioManager.FLAG_SHOW_UI);

        AlexaManager.getInstance(context).sendVolumeChangedEvent(vol, isMute(), null);
        /*
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(adjust) {
                    Toast.makeText(context, "Volume adjusted.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Volume set to: " + (volume / 10), Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
    }

    private void setMute(final boolean isMute){
        AudioManager am = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        am.setStreamMute(AudioManager.STREAM_MUSIC, isMute);
        AlexaManager.getInstance(context).sendMutedEvent(getVolume(),isMute, null);
        Log.i(TAG, "Mute set to : " + isMute);
        /*
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Volume " + (isMute ? "muted" : "unmuted"), Toast.LENGTH_SHORT).show();
            }
        });
        */


    }

    public void controlKK(String directive) {
        com.konka.alexa.alexalib.utility.KKController.controlAction(context, directive);
        /*Looper.prepare();
        Toast.makeText(context, "intent is " + mainTitle, Toast.LENGTH_SHORT).show();
        Looper.loop();*/
        com.konka.alexa.alexalib.interfaces.response.ResponseParser.kkDirective = null;
    }

    private OnAudioPlayerListener mOnAudioPlayerListener;
    public void setmOnAudioPlayerListener(OnAudioPlayerListener onAudioPlayerListener) {
        mOnAudioPlayerListener = onAudioPlayerListener;
    }

    public interface OnAudioPlayerListener {
        void stop();
    }
}
