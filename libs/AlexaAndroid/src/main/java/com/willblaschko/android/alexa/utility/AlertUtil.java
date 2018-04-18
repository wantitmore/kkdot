package com.willblaschko.android.alexa.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.willblaschko.android.alexa.beans.AlertBean;
import com.willblaschko.android.alexa.beans.AlertContextBean;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user001 on 2018-4-12.
 */

public class AlertUtil {
    private static final String TAG  ="AlertUtil";

    public static List<AlertContextBean> getAllAlerts() {
        List<AlertBean> alertBeans = DataSupport.findAll(AlertBean.class);
        Log.d(TAG, "getAllAlerts: " + alertBeans.size());
        List<AlertContextBean> allAlerts = new ArrayList<>();
        for (AlertBean alertBean : alertBeans) {
            AlertContextBean alertContextBean = new AlertContextBean();
            alertContextBean.setToken(alertBean.getToken());
            alertContextBean.setType(alertBean.getType());
            alertContextBean.setScheduledTime(alertBean.getScheduledTime());
            allAlerts.add(alertContextBean);

        }
        return allAlerts;
    }
    public static List<AlertContextBean> getActiveAlerts() {
        List<AlertContextBean> activeAlerts = new ArrayList<>();
        List<AlertBean> alertBeans = DataSupport.findAll(AlertBean.class);
        for (AlertBean alertBean : alertBeans) {
            Log.d(TAG, "getActiveAlerts: " + alertBean.isActive());
            if (alertBean.isActive()) {
                AlertContextBean alertContextBean = new AlertContextBean();
                alertContextBean.setToken(alertBean.getToken());
                alertContextBean.setType(alertBean.getType());
                alertContextBean.setScheduledTime(alertBean.getScheduledTime());
                activeAlerts.add(alertContextBean);
            }
        }
        return activeAlerts;
    }

    public static Boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        return  !(networkinfo == null || !networkinfo.isAvailable());
    }

    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();
        } else if (file.exists()) {
            file.delete();
        }
    }

}
