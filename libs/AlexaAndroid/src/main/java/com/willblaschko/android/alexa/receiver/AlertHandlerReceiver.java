package com.willblaschko.android.alexa.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AlertHandlerReceiver extends BroadcastReceiver {

    private static final String TAG = "AlertHandlerReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onStartCommand: ======== " + bundle);
        //intent.putExtra("tag", "deleteAlert");
        String tag = intent.getStringExtra("tag");
        if (bundle != null && "deleteAlert".equals(tag)) {
            int deleteId = intent.getIntExtra("id", -1);
            String token = intent.getStringExtra("token");
            if (deleteId >= 0) {
                cancelAlertTask(deleteId, token);
            }
        } else if(bundle != null) {
            getAttrs(bundle);
            setAlertTask();
        }
    }
}
