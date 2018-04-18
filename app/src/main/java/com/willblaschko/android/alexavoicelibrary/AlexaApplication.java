package com.willblaschko.android.alexavoicelibrary;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.liulishuo.filedownloader.FileDownloader;
import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.beans.UnSendBean;
import com.willblaschko.android.alexa.data.Event;
import com.willblaschko.android.alexavoicelibrary.utility.SigningKey;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * An application to handle all our initialization for the Alexa library before we
 * launch our VoiceLaunchActivity
 */
public class AlexaApplication extends Application {

    //Our Amazon application product ID, this is passed to the server when we authenticate
    private static final String PRODUCT_ID = "interactive_conversation";
    private static final String TAG = "AlexaApplication";
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private boolean netFlag;

    //Our Application instance if we need to reference it directly
    private static AlexaApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        LitePal.initialize(this);
        //if we run in DEBUG mode, we can get our signing key in the LogCat
        if(BuildConfig.DEBUG){
            Log.i("AlexaApplication", SigningKey.getCertificateMD5Fingerprint(this));
        }
        FileDownloader.setup(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkConnectionReceiver(), intentFilter);
    }



    /**
     * Return a reference to our mInstance instance
     * @return our current application instance, created in onCreate()
     */
    public static AlexaApplication getInstance(){
        return mInstance;
    }

    class NetworkConnectionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: action is " + action);
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                Log.d(TAG, "==========网络状态已改变 " + (info != null ? info.isAvailable() : null));
                if (info != null && info.isAvailable() && !netFlag) {
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
                } else if (info == null || !info.isAvailable()){
                    netFlag = false;
                    Log.d(TAG,"==========没有可用网络");
                }
            }
        }
    }

}
