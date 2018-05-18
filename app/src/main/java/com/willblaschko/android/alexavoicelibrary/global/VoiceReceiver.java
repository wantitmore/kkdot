package com.willblaschko.android.alexavoicelibrary.global;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.WindowManager;

import com.willblaschko.android.alexa.beans.AlertBean;
import com.willblaschko.android.alexavoicelibrary.LoginActivity;
import com.willblaschko.android.alexavoicelibrary.display.DisplayService;
import com.willblaschko.android.alexavoicelibrary.widget.CircleVoiceStateView;

import org.litepal.crud.DataSupport;

import java.util.List;

public class VoiceReceiver extends BroadcastReceiver {

    private static final String VOICE_START = "com.konka.android.intent.action.START_VOICE";
    private static final String VOICE_STOP = "com.konka.android.intent.action.STOP_VOICE";
    private static final String TAG = "VoiceReceiver";
    private CircleVoiceStateView mLayout;
    WindowManager mWindowManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (VOICE_START.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: VOICE_START");
            SharedPreferences alexa = context.getSharedPreferences(Constants.ALEXA, Context.MODE_PRIVATE);
            boolean needLogin = alexa.getBoolean(Constants.LOGIN, true);
            if (needLogin) {
                Intent voiceIntent = new Intent(context, LoginActivity.class);
                voiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                voiceIntent.putExtra("fromVoiceReceiver", true);
                context.startActivity(voiceIntent);
            } else {
                context.startService(new Intent(context, DisplayService.class));
            }


        } else if (VOICE_STOP.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: VOICE_STOP");
            //stop alert if it is playing, assume it is playing
            List<AlertBean> alertBeans = DataSupport.findAll(AlertBean.class);
            for (AlertBean alertBean : alertBeans) {
                boolean active = alertBean.isActive();
                Log.d(TAG, "onReceive: activ--" + active);
                /*if (active) {
                    int deleteId = alertBean.getId();
                    String token = alertBean.getToken();
                    DataSupport.delete(AlertBean.class, deleteId);
                    AlexaManager.getInstance(context).sendEvent(Event.getAlertStoppedEvent(token), null);
                    Intent serviceIntent = new Intent(context, AlertHandlerService.class);
                    intent.putExtra("active", true);
                    context.startService(serviceIntent);
                }*/
            }


        }
    }

}
