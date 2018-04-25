package com.willblaschko.android.alexavoicelibrary.global;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.beans.AlertBean;
import com.willblaschko.android.alexa.data.Event;
import com.willblaschko.android.alexa.service.AlertHandlerService;
import com.willblaschko.android.alexavoicelibrary.display.DisplayCardActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

public class VoiceReceiver extends BroadcastReceiver {

    private static final String VOICE_START = "com.konka.android.intent.action.START_VOICE";
    private static final String VOICE_STOP = "com.konka.android.intent.action.STOP_VOICE";
    private static final String TAG = "VoiceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (VOICE_START.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: VOICE_START");
            Intent voiceIntent = new Intent(context, DisplayCardActivity.class);
            voiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(voiceIntent);
        } else if (VOICE_STOP.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: VOICE_STOP");
            //stop alert if it is playing, assume it is playing
            List<AlertBean> alertBeans = DataSupport.findAll(AlertBean.class);
            for (AlertBean alertBean : alertBeans) {
                boolean active = alertBean.isActive();
                Log.d(TAG, "onReceive: activ--" + active);
                if (active) {
                    int deleteId = alertBean.getId();
                    String token = alertBean.getToken();
                    DataSupport.delete(AlertBean.class, deleteId);
                    AlexaManager.getInstance(context).sendEvent(Event.getAlertStoppedEvent(token), null);
                    Intent serviceIntent = new Intent(context, AlertHandlerService.class);
                    intent.putExtra("active", true);
                    context.startService(serviceIntent);
                }
            }


        }
    }
}
