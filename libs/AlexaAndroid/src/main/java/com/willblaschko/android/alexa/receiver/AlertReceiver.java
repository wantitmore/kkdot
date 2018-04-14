package com.willblaschko.android.alexa.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.willblaschko.android.alexa.service.ResetAlertService;

/**
 * Created by user001 on 2018-4-10.
 */

public class AlertReceiver extends BroadcastReceiver {

    private static final String TAG = "AlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: -------------------" + intent.getAction());
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            context.startService(new Intent(context, ResetAlertService.class));
        }
    }
}
