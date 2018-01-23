package com.willblaschko.android.alexa.utility;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.mstar.android.tv.TvCommonManager;

/**
 * Created by user001 on 2018-1-15.
 */

public class KKController {
    private static final String TAG = "KKController";

    //    ResponseParser
    public static void controlAction(Context context, String directive) {
        Gson gson = new Gson();
        Bean bean = gson.fromJson(directive, Bean.class);
        String mainTitle = bean.getDirective().getPayload().getTitle().getMainTitle();
        Log.d(TAG, "controlKK: " + mainTitle);
        if (mainTitle.contains("PowerIntent")) {// power off TV
            /*PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE); //reset TV
            if (pm != null) {
                pm.reboot(null);
            }*/
            Intent powerOffIntent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
            powerOffIntent.putExtra("android.intent.extra.KEY_CONFIRM", false);
            powerOffIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(powerOffIntent);
        } else if (mainTitle.contains("InputIntent")) {
            String textField = bean.getDirective().getPayload().getTextField();
            if (textField.contains("input AV")) {
                TVHelper.setInputSourceThroughTvPlayer(context, TvCommonManager.INPUT_SOURCE_CVBS);
            } else if (textField.contains("input DTV")) {
                TVHelper.setInputSourceThroughTvPlayer(context, TvCommonManager.INPUT_SOURCE_DTV);
            } else if (textField.contains("input ATV")) {
                TVHelper.setInputSourceThroughTvPlayer(context, TvCommonManager.INPUT_SOURCE_ATV);
            } else if (textField.contains("input YPBPR")) {
                TVHelper.setInputSourceThroughTvPlayer(context, TvCommonManager.INPUT_SOURCE_YPBPR);
            } else if (textField.contains("input VGA")) {
                TVHelper.setInputSourceThroughTvPlayer(context, TvCommonManager.INPUT_SOURCE_VGA);
            } else if (textField.contains("input HDMI 1")) {
                TVHelper.setInputSourceThroughTvPlayer(context, TvCommonManager.INPUT_SOURCE_HDMI);
            } else if (textField.contains("input HDMI 2")) {
                TVHelper.setInputSourceThroughTvPlayer(context, TvCommonManager.INPUT_SOURCE_HDMI2);
            } else if (textField.contains("input HDMI 3")) {
                TVHelper.setInputSourceThroughTvPlayer(context, TvCommonManager.INPUT_SOURCE_HDMI3);
            }
        }
    }
}
