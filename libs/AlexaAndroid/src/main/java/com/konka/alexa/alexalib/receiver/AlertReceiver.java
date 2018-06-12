package com.konka.alexa.alexalib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.konka.alexa.alexalib.service.ResetAlertService;

/**
 * Created by user001 on 2018-4-10.
 */

public class AlertReceiver extends BroadcastReceiver {

    private static final String TAG = "AlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //check login
        SharedPreferences alexa = context.getSharedPreferences("alexa", Context.MODE_PRIVATE);
        boolean needLogin = alexa.getBoolean("need_login", true);
        Log.d(TAG, "onReceive: -------------------" + intent.getAction() + "-->" + needLogin);
        if (needLogin) {
            return;
        }
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            context.startService(new Intent(context, ResetAlertService.class));
        }
    }
}
