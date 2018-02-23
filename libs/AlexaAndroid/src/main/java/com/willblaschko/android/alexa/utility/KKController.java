package com.willblaschko.android.alexa.utility;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.gson.Gson;
import com.mstar.android.tv.TvCommonManager;
import com.willblaschko.android.alexa.R;

import java.util.ArrayList;
import java.util.List;

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
            executePowerIntent();
        } else if (mainTitle.contains("InputIntent")) {
            executeInputIntent(context, textField);
        } else if (mainTitle.contains("ChannelIntent")) {
            // switch channel
            executeChannelIntent(textField);
        } else if (mainTitle.contains("AppIntent")) {
            // control app
            Log.d(TAG, "controlAction: app name is " + textField);
            int indexName = textField.lastIndexOf("name");
            String appName = textField.substring(indexName + 4).trim();
            ArrayList<AppInfo> appInfoList = getAppList(context, appName);
            ActionResult actionResult = new ActionResult(ControlActions.ACTION_OPEN_APP);
            actionResult.setmAppInfoList(appInfoList);
            if (appInfoList.size() == 0){
                actionResult.setmActionMessage(context.getString(R.string.open_app_not_found));
            }
            else if(appInfoList.size() == 1){
                // TODO: need to be improved
                actionResult.setmActionMessage(context.getString(R.string.open_app_hint_p1) + " " + appName + " " + context.getString(R.string.open_app_hint_p2));
            }
            else if(appInfoList.size() >= 2){
                // need message to UI
                // and do the action according the user's choice
                actionResult.setmActionMessage(context.getString(R.string.open_app_choose));
            }
            /*PackageManager pm = context.getPackageManager();
            // Return a List of all packages that are installed on the device.
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            for (PackageInfo packageInfo : packages) {
                System.out.println("MainActivity.getAppList, packageInfo=" + packageInfo.packageName + ", " + packageInfo.applicationInfo.loadLabel(context.getPackageManager()));
            }*/
        }
    }

    public static ArrayList<AppInfo> getAppList(Context context, String appName){
        appName = appName.toUpperCase();
        ArrayList<AppInfo> list = new ArrayList<>();

        if (appName.equals("FILE MANAGER") || appName.equals("MUSIC")
                || appName.equals("ALBUM") || appName.equals("VIDEO")
                || appName.equals("HOME SHARE") || appName.equals("DLNA")){
            AppInfo appInfo = new AppInfo(appName, "com.konka.multimedia");
            list.add(appInfo);
            return list;
        }
        else if(appName.equals("VOICE SEARCH") || appName.equals("GOOGLE")){
            AppInfo appInfo = new AppInfo(appName, "com.google.android.googlequicksearchbox");
            list.add(appInfo);
            return list;
        }

        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        for (PackageInfo packageInfo : packageInfoList){
            String label = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            if (label.toUpperCase().contains(appName)){
                AppInfo appInfo = new AppInfo(label, packageInfo.applicationInfo.packageName);
                list.add(appInfo);
            }
        }
        return list;
    }

/*    public boolean openApp(String packageName){
        boolean ret = true;
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        mContext.startActivity(intent);
        return ret;
    }*/

    private static void executePowerIntent() {
        Intent powerOffIntent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        powerOffIntent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        powerOffIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(powerOffIntent);
    }

    private static void executeChannelIntent(String textField) {
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

    private static void executeInputIntent(Context context, String textField) {
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
