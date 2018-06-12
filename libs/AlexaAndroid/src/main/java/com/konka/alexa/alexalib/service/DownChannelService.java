package com.konka.alexa.alexalib.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.konka.alexa.alexalib.AlexaManager;
import com.konka.alexa.alexalib.TokenManager;
import com.konka.alexa.alexalib.callbacks.ImplAsyncCallback;
import com.konka.alexa.alexalib.connection.ClientUtil;
import com.konka.alexa.alexalib.data.Directive;
import com.konka.alexa.alexalib.data.Event;
import com.konka.alexa.alexalib.interfaces.AvsItem;
import com.konka.alexa.alexalib.interfaces.AvsResponse;
import com.konka.alexa.alexalib.interfaces.response.ResponseParser;
import com.konka.alexa.alexalib.system.AndroidSystemHandler;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSource;

/**
 * @author will on 4/27/2016.
 */

public class DownChannelService extends Service {

    private static final String TAG = "DownChannelService";

    private AlexaManager alexaManager;
    private Call currentCall;
    private AndroidSystemHandler handler;
    private Handler runnableHandler;
    private Runnable pingRunnable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Launched");
        alexaManager = AlexaManager.getInstance(this);
        handler = AndroidSystemHandler.getInstance(this);

        runnableHandler = new Handler(Looper.getMainLooper());
        pingRunnable = new Runnable() {
            @Override
            public void run() {
                TokenManager.getAccessToken(alexaManager.getAuthorizationManager().getAmazonAuthorizationManager(), DownChannelService.this, new TokenManager.TokenCallback() {
                    @Override
                    public void onSuccess(String token) {

                        Log.i(TAG, "Sending heartbeat");
                        final Request request = new Request.Builder()
                                .url(alexaManager.getPingUrl())
                                .get()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();

                        ClientUtil.getTLS12OkHttpClient()
                                .newCall(request)
                                .enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        runnableHandler.postDelayed(pingRunnable, 4 * 60 * 1000);
                                    }
                                });
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                });
            }
        };

        openDownChannel();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(currentCall != null){
            currentCall.cancel();
        }
        runnableHandler.removeCallbacks(pingRunnable);
    }


    private void openDownChannel(){
        TokenManager.getAccessToken(alexaManager.getAuthorizationManager().getAmazonAuthorizationManager(), DownChannelService.this, new TokenManager.TokenCallback() {
            @Override
            public void onSuccess(String token) {

                OkHttpClient downChannelClient = ClientUtil.getTLS12OkHttpClient();

                final Request request = new Request.Builder()
                        .url(alexaManager.getDirectivesUrl())
                        .get()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();

                currentCall = downChannelClient.newCall(request);
                currentCall.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: error is " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, "onResponse: code-->" + response.code());
                        alexaManager.sendEvent(Event.getSynchronizeStateEvent(), new ImplAsyncCallback<AvsResponse, Exception>() {
                            @Override
                            public void success(AvsResponse result) {
                                handler.handleItems(result, true);
                                runnableHandler.post(pingRunnable);
                            }
                        });

                        BufferedSource bufferedSource = response.body().source();

                        while (!bufferedSource.exhausted()) {
                            String line = bufferedSource.readUtf8Line();
                            try {
                                Log.d(TAG, "onResponse: line is " + line);
                                Directive directive = ResponseParser.getDirective(line);
                                AvsResponse responses = handler.handleDirective(directive);
                                EventBus.getDefault().post(responses);
                                //surface to our UI if it's up
                                try {
                                    AvsItem item = ResponseParser.parseDirective(directive);
                                    EventBus.getDefault().post(item);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Bad line");
                            }
                        }

                    }
                });

            }

            @Override
            public void onFailure(Throwable e) {
                e.printStackTrace();
            }
        });
    }

}
