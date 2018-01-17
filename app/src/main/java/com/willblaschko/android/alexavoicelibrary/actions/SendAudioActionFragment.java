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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.willblaschko.android.alexa.requestbody.DataRequestBody;
import com.willblaschko.android.alexavoicelibrary.BuildConfig;
import com.willblaschko.android.alexavoicelibrary.R;
import com.willblaschko.android.recorderview.RecorderView;

import java.io.IOException;

import ee.ioc.phon.android.speechutils.RawAudioRecorder;
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
        recorderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recorder == null) {
                    startListening();
                }else{
                    stopListening();
                }
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.konka.android.intent.action.START_VOICE");
        filter.addAction("com.konka.android.intent.action.STOP_VOICE");
        alexaReceiver = new AlexaReceiver();
        getContext().registerReceiver(alexaReceiver, filter);
    }

    @Override
    public void onResume() {
        super.onResume();

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
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
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
    }

    @Override
    public void onStop() {
        super.onStop();
        //tear down our recorder on stop
        if(recorder != null){
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    @Override
    public void startListening() {
        if(recorder == null){
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
                    final float rmsdb = recorder.getRmsdb();
                    if(recorderView != null) {
                        recorderView.post(new Runnable() {
                            @Override
                            public void run() {
                                recorderView.setRmsdbLevel(rmsdb);
                                Log.d(TAG, "run: ----rmsdb is " + rmsdb);
                                if (rmsdb <= 0) {
                                    mMicrophone.setVisibility(View.VISIBLE);
                                } else {
                                    mMicrophone.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                    if(sink != null && recorder != null) {
                        sink.write(recorder.consumeRecording());
                    }
                    if(BuildConfig.DEBUG){
                        Log.i(TAG, "Received audio");
                        Log.i(TAG, "RMSDB: " + rmsdb);
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

    private void stopListening(){
        if(recorder != null) {
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
            if(intent.getAction().equals("com.konka.android.intent.action.START_VOICE")) {
                startListening();
            }
            if(intent.getAction().equals("com.konka.android.intent.action.STOP_VOICE")) {
                stopListening();
            }
        }
    }

}
