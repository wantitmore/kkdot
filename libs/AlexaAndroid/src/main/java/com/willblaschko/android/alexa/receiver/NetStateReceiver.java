package com.willblaschko.android.alexa.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.beans.UnSendBean;
import com.willblaschko.android.alexa.data.Event;

import org.litepal.crud.DataSupport;

import java.util.List;

public class NetStateReceiver extends BroadcastReceiver {

    private static final String TAG = "NetStateReceiver";
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private boolean netFlag;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        Log.d(TAG, "onReceive: action is " + action);
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
//            reSendEvent(context);
        }
    }

    private void reSendEvent(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        info = connectivityManager.getActiveNetworkInfo();
        Log.d(TAG, "==========网络状态已改变 " + (info != null ? info.isAvailable() : null));
        if (info != null && info.isAvailable() /*&& !netFlag*/) {
            netFlag = true;
            Log.d(TAG, "==========当前网络名称:"+ info.getTypeName());
            List<UnSendBean> unSendBeans = DataSupport.findAll(UnSendBean.class);
            for (UnSendBean bean :  unSendBeans) {
                String type = bean.getType();
                String token = bean.getToken();
                if ("AlertStopped".equals(type)) {
                    AlexaManager.getInstance(context).sendEvent(Event.getAlertStoppedEvent(token), null);
                } else if ("AlertStarted".equals(type)) {
                    AlexaManager.getInstance(context).sendEvent(Event.getAlertStartedEvent(token), null);
                }
//                    DataSupport.deleteAll(UnSendBean.class, "token = ?", token);
            }
        } else if (info != null && !info.isAvailable()){
            netFlag = false;
            Log.d(TAG,"==========没有可用网络");
        }
    }
}
