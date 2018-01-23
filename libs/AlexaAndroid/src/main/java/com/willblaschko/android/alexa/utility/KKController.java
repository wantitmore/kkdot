package com.willblaschko.android.alexa.utility;

import android.content.Context;
import android.content.Intent;

/**
 * Created by user001 on 2018-1-15.
 */

public class KKController {
//    ResponseParser
    public static void controlAction(Context context, String intent) {
        if (intent.contains("PowerIntent")) {// power off TV
            /*PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE); //reset TV
            if (pm != null) {
                pm.reboot(null);
            }*/
            Intent powerOffIntent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
            powerOffIntent.putExtra("android.intent.extra.KEY_CONFIRM", false);
            powerOffIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(powerOffIntent);
        }
    }
}
