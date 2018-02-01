package com.willblaschko.android.alexa.utility;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.willblaschko.android.alexa.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stone on 17-6-28.
 */

public class AppModule {

    private Context mContext = null;

    private Handler mAppHandler = null;


    public AppModule(Context context){
        this.mContext = context;
        // mAppHandler = new AppHandler();
    }

    public ArrayList<AppInfo> getAppList(String appName){
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

        PackageManager packageManager = mContext.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        for (PackageInfo packageInfo : packageInfoList){
            String label = packageInfo.applicationInfo.loadLabel(packageManager).toString();
//            if(packageInfo.applicationInfo.name == null){
//                VCLog.v("name null package name:" + packageInfo.packageName + " label" + packageInfo.applicationInfo.loadLabel(packageManager));
//                continue;
//            }
            // VCLog.v("app name:" + label + " : appName:" + appName);
            if (label.toUpperCase().contains(appName)){
                AppInfo appInfo = new AppInfo(label, packageInfo.applicationInfo.packageName);
                list.add(appInfo);
            }
        }
        return list;
    }

    public boolean openAppWithAppName(String appName){
        boolean ret = false;
        ArrayList<AppInfo> appInfoList = getAppList(appName);
        ActionResult actionResult = new ActionResult(ControlActions.ACTION_OPEN_APP);
        actionResult.setmAppInfoList(appInfoList);
        if (appInfoList.size() == 0){
            actionResult.setmActionMessage(mContext.getString(R.string.open_app_not_found));
        }
        else if(appInfoList.size() == 1){
            // TODO: need to be improved
            actionResult.setmActionMessage(mContext.getString(R.string.open_app_hint_p1) + " " + appName + " " + mContext.getString(R.string.open_app_hint_p2));

            // open action in UI module, use methods in class AppHelper
//            final String pkgName = appInfoList.get(0).getmPackageName();
//            mAppHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    openApp(pkgName);
//                }
//            }, AppHelper.DELAY_OPEN_APP);
        }
        else if(appInfoList.size() >= 2){
            // need message to UI
            // and do the action according the user's choice
            actionResult.setmActionMessage(mContext.getString(R.string.open_app_choose));
            // actionResult.setmAppInfoList(appInfoList);
        }
//        mActionResultCallback.actionResult(actionResult);
        return ret;
    }

    public boolean uninstallAppWithAppName(String appName){
        boolean ret  = false;
        ArrayList<AppInfo> appInfoList = getAppList(appName);
        ActionResult actionResult = new ActionResult(ControlActions.ACTION_UNINSTALL_APP);
        actionResult.setmAppInfoList(appInfoList);
        int appListSize = appInfoList.size();
        if (appListSize == 0){
            actionResult.setmActionMessage(mContext.getString(R.string.open_app_not_found));
        }
        else if(appListSize == 1){
            actionResult.setmActionMessage(mContext.getString(R.string.open_app_hint_p1) + " " + appName + " " + mContext.getString(R.string.open_app_hint_p2));
        }
        else {
            actionResult.setmActionMessage(mContext.getString(R.string.uninstall_app_choose));
        }
//        mActionResultCallback.actionResult(actionResult);
        return ret;
    }

    public boolean installAppWithAppName(String appName){
        boolean ret = false;

        return ret;
    }

    public boolean openApp(String packageName){
        boolean ret = true;
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        mContext.startActivity(intent);
        return ret;
    }
}
