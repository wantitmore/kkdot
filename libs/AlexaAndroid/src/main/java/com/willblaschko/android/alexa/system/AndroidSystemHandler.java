package com.willblaschko.android.alexa.system;

import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;

import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.beans.AlertBean;
import com.willblaschko.android.alexa.callbacks.ImplAsyncCallback;
import com.willblaschko.android.alexa.data.Directive;
import com.willblaschko.android.alexa.data.Event;
import com.willblaschko.android.alexa.interfaces.AvsItem;
import com.willblaschko.android.alexa.interfaces.AvsResponse;
import com.willblaschko.android.alexa.interfaces.alerts.AvsDeleteAlertItem;
import com.willblaschko.android.alexa.interfaces.alerts.AvsSetAlertItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsMediaNextCommandItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsMediaPauseCommandItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsMediaPlayCommandItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsMediaPreviousCommandItem;
import com.willblaschko.android.alexa.interfaces.response.ResponseParser;
import com.willblaschko.android.alexa.interfaces.speaker.AvsAdjustVolumeItem;
import com.willblaschko.android.alexa.interfaces.speaker.AvsSetMuteItem;
import com.willblaschko.android.alexa.interfaces.speaker.AvsSetVolumeItem;
import com.willblaschko.android.alexa.interfaces.system.AvsSetEndpointItem;
import com.willblaschko.android.alexa.receiver.AlertHandlerReceiver;
import com.willblaschko.android.alexa.service.DownChannelService;
import com.willblaschko.android.alexa.utility.KKController;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
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
    public void handleItems(@NonNull AvsResponse response){
        for(AvsItem current: response){

            Log.i(TAG, "Handling AvsItem: " + current.getClass());
            if(current instanceof AvsSetEndpointItem){
                Log.i(TAG, "Setting URL endpoint: " + ((AvsSetEndpointItem) current).getEndpoint());
                AlexaManager.getInstance(context)
                        .setUrlEndpoint(((AvsSetEndpointItem) current).getEndpoint());

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
            }else if(current instanceof AvsMediaPlayCommandItem){
                //fake a hardware "play" press
                sendMediaButton(KeyEvent.KEYCODE_MEDIA_PLAY);
                Log.i(TAG, "Media play command issued");
            }else if(current instanceof AvsMediaPauseCommandItem){
                //fake a hardware "pause" press
                sendMediaButton(KeyEvent.KEYCODE_MEDIA_PAUSE);
                Log.i(TAG, "Media pause command issued");
            }else if(current instanceof AvsMediaNextCommandItem){
                //fake a hardware "next" press
                sendMediaButton(KeyEvent.KEYCODE_MEDIA_NEXT);
                Log.i(TAG, "Media next command issued");
            }else if(current instanceof AvsMediaPreviousCommandItem){
                //fake a hardware "previous" press
                sendMediaButton(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
                Log.i(TAG, "Media previous command issued");
            }else if (current instanceof AvsSetAlertItem){
                Log.d(TAG, "handleItems: current item is " + ((AvsSetAlertItem) current).getType() + "--");
                if(((AvsSetAlertItem) current).isAlarm()){
                    Log.d(TAG, "handleItems: -------------->");
                    setAlarm((AvsSetAlertItem) current);
                }else if(((AvsSetAlertItem) current).isTimer()){
                    setTimer((AvsSetAlertItem) current);
                }
            }else if (current instanceof AvsDeleteAlertItem){
                Log.d(TAG, "handleItems: ---deleteAlert");
                deleteAlert(current);
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
            /*boolean active = alertBeans.get(0).isActive();
            if (active) {
                context.bindService()
            }*/
            Intent intent = new Intent(context, AlertHandlerReceiver.class);

            intent.putExtra("tag", "deleteAlert");
            intent.putExtra("id", id);
            intent.putExtra("token", token);
            context.sendBroadcast(intent);
        }
    }


    public void handleDirective(Directive directive){
        try {
            AvsItem item = ResponseParser.parseDirective(directive);
            handleItem(item);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleItem(AvsItem item){
        if(item == null){
            return;
        }
        AvsResponse response = new AvsResponse();
        response.add(item);
        handleItems(response);
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

/*    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            AlertService.AlertBinder alertService = (AlertService.AlertBinder) service;
            alertService.stopPlayer();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };*/

    private void setTimer(final AvsSetAlertItem item){
        try {
            int time = (int) ((item.getScheduledTimeMillis() - System.currentTimeMillis()) / 1000);
            Log.d(TAG, "setTimer: time is " + time);
            Intent alarmas = new Intent(AlarmClock.ACTION_SET_TIMER);
            alarmas.putExtra(AlarmClock.EXTRA_LENGTH, time);
            alarmas.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
            alarmas.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(alarmas);
            AlexaManager.getInstance(context)
                    .sendEvent(Event.getSetAlertSucceededEvent(item.getToken()), null);

            //cheating way to tell Alexa that the timer happened successfully--this SHOULD be improved
            //todo make this better
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    AlexaManager.getInstance(context)
                            .sendEvent(Event.getAlertStartedEvent(item.getToken()), new ImplAsyncCallback<AvsResponse, Exception>() {
                                @Override
                                public void complete() {
                                    AlexaManager.getInstance(context)
                                            .sendEvent(Event.getAlertStoppedEvent(item.getToken()), null);
                                }
                            });
                }
            }, time * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    private void setAlarm(AvsSetAlertItem item){
        Log.d(TAG, "setAlarm: ==========");

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
        KKController.controlAction(context, directive);
        /*Looper.prepare();
        Toast.makeText(context, "intent is " + mainTitle, Toast.LENGTH_SHORT).show();
        Looper.loop();*/
        ResponseParser.kkDirective = null;
    }
}
