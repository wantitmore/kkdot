package com.konka.alexa.utility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user001 on 2018-1-19.
 */

public class AppController {
    private Context mContext;
    static  String TAG = "ApkTool";
    public static List<MyAppInfo> mLocalInstallApps = null;

    public static List<MyAppInfo> scanLocalInstallAppList(PackageManager packageManager) {
        List<MyAppInfo> myAppInfos = new ArrayList<>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            Log.d(TAG, "scanLocalInstallAppList: size is " + packageInfos.size());
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
//            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
//                continue;
//            }
                MyAppInfo myAppInfo = new MyAppInfo();
                myAppInfo.setAppName(packageInfo.packageName);
//                Log.d(TAG, "scanLocalInstallAppList: name is " + packageInfo.packageName);
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                myAppInfo.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                myAppInfos.add(myAppInfo);
            }
        }catch (Exception e){
            Log.e(TAG,"===============获取应用包信息失败");
        }
        return myAppInfos;
    }

    static class MyAppInfo {
        private Drawable image;
        private String appName;

        public MyAppInfo(Drawable image, String appName) {
            this.image = image;
            this.appName = appName;
        }
        public MyAppInfo() {

        }

        public Drawable getImage() {
            return image;
        }

        public void setImage(Drawable image) {
            this.image = image;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }
    }
}
