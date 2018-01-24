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
        String textField = bean.getDirective().getPayload().getTextField();
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
        } else if (mainTitle.contains("ChannelIntent")) {
            // switch channel
            String channelName;
            if (textField.contains("last")) {
                channelName = TVHelper.CMD_LAST_CHANNEL;
            } else if (textField.contains("next")) {
                channelName = TVHelper.CMD_NEXT_CHANNEL;
            } else if (textField.contains("previous")) {
                channelName = TVHelper.CMD_PREVIOUS_CHANNEL;
            } else if (textField.contains("first") || textField.contains("1ST")) {
                channelName = TVHelper.CMD_FIRST_CHANNEL;
            } else {
                int channelIndex = textField.indexOf("channel");
                channelName = textField.toUpperCase().substring(channelIndex + 7).trim().toUpperCase();
            }
            Log.d(TAG, "controlAction: channelName is " + channelName);
            boolean isFoundTheChannel = false;
            if (channelName.equals(TVHelper.CMD_LAST_CHANNEL)) {
                isFoundTheChannel = TVHelper.toLastChannel();
            } else if (channelName.equals(TVHelper.CMD_NEXT_CHANNEL)) {
                isFoundTheChannel = TVHelper.toNextChannel();
            } else if (channelName.equals(TVHelper.CMD_PREVIOUS_CHANNEL)) {
                isFoundTheChannel = TVHelper.toPreviousChannel();
            } else if (channelName.equals(TVHelper.CMD_FIRST_CHANNEL)) {
                isFoundTheChannel = TVHelper.toFirstChannel();
            } else {
                // the last condition: use channel name
                isFoundTheChannel = TVHelper.toDTVSpecialChannel(channelName);
            }


            ActionResult actionResult = new ActionResult(ControlActions.ACTION_CHANGE_CHANNEL);
            if (isFoundTheChannel) {
                Log.d(TAG, "controlAction: find channel " + channelName);
                actionResult.setmActionMessage("channel to " + channelName);
            } else {
                Log.d(TAG, "controlAction: can not found channel " + channelName);
                actionResult.setmActionMessage("can not found the channel:" + channelName);
            }
//            mActionResultCallback.actionResult(actionResult);
        }
    }
}
