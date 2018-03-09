package com.willblaschko.android.alexavoicelibrary.actions;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.willblaschko.android.alexa.beans.Template1Bean;
import com.willblaschko.android.alexa.beans.Template2Bean;
import com.willblaschko.android.alexa.requestbody.DataRequestBody;
import com.willblaschko.android.alexavoicelibrary.BuildConfig;
import com.willblaschko.android.alexavoicelibrary.R;
import com.willblaschko.android.recorderview.RecorderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import ee.ioc.phon.android.speechutils.RawAudioRecorder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;


/**
 * @author will on 5/30/2016.
 */

public class SendAudioActionFragment extends BaseListenerFragment {

    private static final String TAG = "SendAudioActionFragment";

    private final static int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private static final int AUDIO_RATE = 16000;
    private RawAudioRecorder recorder;
    private RecorderView recorderView;
    private AlexaReceiver alexaReceiver;
    private ImageView mMicrophone;
    private TextView mMainTitle;
    private TextView mSubTitle;
    private TextView mContent;
    private ImageView mImageView;
    private LinearLayout mTemplate2;
    private LinearLayout mMicroLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_action_audio, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recorderView = (RecorderView) view.findViewById(R.id.recorder);
        mMicrophone = (ImageView) view.findViewById(R.id.iv_microphone);
        mMicroLayout = (LinearLayout) view.findViewById(R.id.ll_micro);
        mTemplate2 = (LinearLayout) view.findViewById(R.id.ll_displayTemplate2);
        mMainTitle = (TextView) view.findViewById(R.id.tv_mainTitle);
        mSubTitle = (TextView) view.findViewById(R.id.tv_subTitle);
        mContent = (TextView) view.findViewById(R.id.tv_content);
        mImageView = (ImageView) view.findViewById(R.id.iv_icon);
        recorderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recorder == null) {
                    startListening();
                } else {
                    stopListening();
                }
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.konka.android.intent.action.START_VOICE");
        filter.addAction("com.konka.android.intent.action.STOP_VOICE");
        alexaReceiver = new AlexaReceiver();
        getContext().registerReceiver(alexaReceiver, filter);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        mMicroLayout.setVisibility(View.VISIBLE);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                }
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (alexaReceiver != null) {
            getContext().unregisterReceiver(alexaReceiver);
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //tear down our recorder on stop
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    @Override
    public void startListening() {
        Log.d(TAG, "startListening: ===");
        if (recorder == null) {
            Log.d(TAG, "startListening: ===2");
            recorder = new RawAudioRecorder(AUDIO_RATE);
        }
        recorder.start();
        alexaManager.sendAudioRequest(requestBody, getRequestCallback());
    }

    private DataRequestBody requestBody = new DataRequestBody() {
        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            Log.d(TAG, "writeTo: recorder is " + recorder + ", is Pa" + ", recordView is " + recorderView);
            while (recorder != null && !recorder.isPausing()) {
                try {
                    Log.d(TAG, "writeTo: record is null? " + recorder);
                    if (recorder != null) {
                        final float rmsdb = recorder.getRmsdb();
                        if (recorderView != null) {
                            Log.d(TAG, "run: ----rmsdb is " + rmsdb);
                            recorderView.post(new Runnable() {
                                @Override
                                public void run() {
                                    recorderView.setRmsdbLevel(rmsdb);
                                    if (rmsdb <= 0) {
                                        mMicrophone.setVisibility(View.VISIBLE);
                                    } else {
                                        mMicrophone.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    }
                    if (sink != null && recorder != null) {
                        sink.write(recorder.consumeRecording());
                    }
                    if (BuildConfig.DEBUG) {
//                        Log.i(TAG, "Received audio");
//                        Log.i(TAG, "RMSDB: " + rmsdb);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "writeTo: error is " + e.getMessage());
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stopListening();
        }

    };

    private void stopListening() {
        Log.d(TAG, "stopListening: --");
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

/*    @Override
    protected String getTitle() {
        return getString(*//*R.string.fragment_action_send_audio*//*R.string.alexa_api);
    }*/

    @Override
    protected int getRawCode() {
        return R.raw.code_audio;
    }

    public class AlexaReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "onReceive: intent is " + intent.getAction());
            if (Objects.equals(intent.getAction(), "com.konka.android.intent.action.START_VOICE")/* && recorder == null*/) {
//                AndroidSystemHandler.welcomeIntent = false;
                startListening();
            }
            if (Objects.equals(intent.getAction(), "com.konka.android.intent.action.STOP_VOICE")) {
                stopListening();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(String directive) {
        // show displaycard info
        boolean template1 = directive.contains("\"type\":\"BodyTemplate2\"");
        Log.d(TAG, "handleEvent: directive is " + directive + "\n\r ==" + template1);
        mMicroLayout.setVisibility(View.GONE);
        mTemplate2.setVisibility(View.VISIBLE);
        Gson gson = new Gson();

        if (directive.contains("\"type\":\"BodyTemplate1\"")) {
            Template1Bean template1Bean = gson.fromJson(directive, Template1Bean.class);
            Template1Bean.DirectiveBean directiveBean = template1Bean.getDirective();
            Template1Bean.DirectiveBean.PayloadBean payload = directiveBean.getPayload();
            String type = directiveBean.getPayload().getType();
            String subTitle = payload.getTitle().getSubTitle();
            String mainTitle = payload.getTitle().getMainTitle();
            String content = payload.getTextField();
            mSubTitle.setText(subTitle);
            mImageView.setVisibility(View.GONE);
            Log.d(TAG, "handleEvent: main title is " +mainTitle + ", subTitle is " + subTitle);
            mMainTitle.setText(mainTitle);
            mContent.setText(content);
        } else if (directive.contains("\"type\":\"BodyTemplate2\"")) {
            Template2Bean template2Bean = gson.fromJson(directive, Template2Bean.class);
            Template2Bean.DirectiveBean directiveBean = template2Bean.getDirective();
            Template2Bean.DirectiveBean.PayloadBean payload = directiveBean.getPayload();
            String type = directiveBean.getPayload().getType();
            String mainTitle = payload.getTitle().getMainTitle();
            String content = payload.getTextField();
//            String imgUrl = payload.getImage().getSourcesBean().getUrl()//null pointer
            List<Template2Bean.DirectiveBean.PayloadBean.ImageBean.SourcesBean> sourcesBeans = payload.getImage().getSources();//null pointer
            for (Template2Bean.DirectiveBean.PayloadBean.ImageBean.SourcesBean bean : sourcesBeans) {
                String url = bean.getUrl();
                if (!TextUtils.isEmpty(url)) {
                    Log.d(TAG, "handleEvent: load image == " + url);
                    Glide.with(getActivity()).load(url).into(mImageView);
                    break;
                }
            }
            mImageView.setVisibility(View.VISIBLE);
            mMainTitle.setText(mainTitle);
            mContent.setText(content);
        }
    }
    public void getDatasync(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    Request request = new Request.Builder()
                            .url(url)//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response;
                    response = client.newCall(request).execute();//得到Response 对象
                    if (response.isSuccessful()) {
                        Log.d("kwwl","response.code()=="+response.code());
                        Log.d("kwwl","response.message()=="+response.message());
                        Log.d("kwwl","res=="+response.body().string());
                        //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

