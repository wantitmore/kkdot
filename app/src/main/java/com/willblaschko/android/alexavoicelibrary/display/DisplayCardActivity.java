package com.willblaschko.android.alexavoicelibrary.display;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.beans.ListTemplate1Bean;
import com.willblaschko.android.alexa.beans.PlayerInfoBean;
import com.willblaschko.android.alexa.beans.Template1Bean;
import com.willblaschko.android.alexa.beans.Template2Bean;
import com.willblaschko.android.alexa.beans.WeatherTemplateBean;
import com.willblaschko.android.alexa.interfaces.response.ResponseParser;
import com.willblaschko.android.alexa.requestbody.DataRequestBody;
import com.willblaschko.android.alexavoicelibrary.BaseActivity;
import com.willblaschko.android.alexavoicelibrary.LoginActivity;
import com.willblaschko.android.alexavoicelibrary.R;
import com.willblaschko.android.alexavoicelibrary.global.Constants;
import com.willblaschko.android.alexavoicelibrary.widget.CircleVoiceStateView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Objects;

import ee.ioc.phon.android.speechutils.RawAudioRecorder;
import okio.BufferedSink;

import static com.willblaschko.android.alexavoicelibrary.global.Constants.PRODUCT_ID;


public class DisplayCardActivity extends BaseActivity {

    private final static String TAG = DisplayCardActivity.class.getSimpleName();
    private static final int AUDIO_RATE = 16000;
    private final static int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private CircleVoiceStateView mVoiceStateView;
    private EditText search;
    private View button;
    private AlexaManager alexaManager;
    private Fragment mShowingFragment;
    private FragmentManager mFragmentManager;
    private RawAudioRecorder recorder;
    private AlexaReceiver alexaReceiver;
    private int mScreenWidth;
    private int mScreenHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences alexa = getSharedPreferences(Constants.ALEXA, MODE_PRIVATE);
        boolean needLogin = alexa.getBoolean(Constants.LOGIN, true);
        if (needLogin) {
            // show education login UI
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        overridePendingTransition(0,R.animator.out_activity);
        WindowManager windowManager = getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        alexaManager = AlexaManager.getInstance(this, PRODUCT_ID);

        setContentView(R.layout.activity_display_card);
        mVoiceStateView = (CircleVoiceStateView) findViewById(R.id.voice_view_state);

        EventBus.getDefault().register(this);

        mFragmentManager = getFragmentManager();


/*        search = (EditText) findViewById(R.id.search);
        button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });*/
        mVoiceStateView.setOnClickListener(new View.OnClickListener() {
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
        registerReceiver(alexaReceiver, filter);
        startListening();
    }

    public void moveVoiceViewToCenter() {
        mVoiceStateView.setX((mScreenWidth - mVoiceStateView.getWidth()) / 2);
    }

    public void resetVoiceViewPosition() {
        Log.d(TAG, "reset position");
        mVoiceStateView.setX(mVoiceStateView.getLeft());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(this,
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
                    Toast.makeText(this, "Record audio permission deny", Toast.LENGTH_SHORT).show();
                }
            }
            default:
                break;
        }
    }

 /*   private void search(){
        String text = search.getText().toString();
        alexaManager.sendTextRequest(text, getRequestCallback());
    }*/


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(Object renderObj) {

        Bundle args = new Bundle();
        if (renderObj instanceof Template1Bean) {
            mShowingFragment = Template1Fragment.newInstance();
        } else if (renderObj instanceof Template2Bean) {
            mShowingFragment = Template2Fragment.newInstance();
        } else if (renderObj instanceof WeatherTemplateBean) {
            mShowingFragment = WeatherTemplateFragment.newInstance();
        } else if (renderObj instanceof ListTemplate1Bean) {
            mShowingFragment = ListTemplate1Fragment.newInstance();
        } else if (renderObj instanceof PlayerInfoBean) {
            mShowingFragment = PlayerInfoFragment.newInstance();
        } else {
            mShowingFragment = EmptyFragment.newInstance();
        }
        if (renderObj instanceof Parcelable) {
            args.putParcelable("args", (Parcelable) renderObj);
            mShowingFragment.setArguments(args);
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.setCustomAnimations()
        transaction.setCustomAnimations(R.animator.enter, R.animator.out);

        transaction.replace(R.id.main_display_content, mShowingFragment).commit();
    }


    private DataRequestBody requestBody = new DataRequestBody() {
        @Override
        public void writeTo(BufferedSink sink) throws IOException {
//            Log.d(TAG, "writeTo: recorder is " + recorder + ", is Pa");
            while (recorder != null && !recorder.isPausing()) {
                try {
//                    Log.d(TAG, "writeTo: record is null? " + recorder);
                    if (recorder != null) {
                        final float rmsdb = recorder.getRmsdb();
                        Log.d(TAG, "run: ----rmsdb is " + rmsdb);
                        mVoiceStateView.post(new Runnable() {
                            @Override
                            public void run() {
                                CircleVoiceStateView.State currentState = mVoiceStateView.getCurrentState();
                                if (currentState != CircleVoiceStateView.State.ACTIVE_LISTENING) {
                                    mVoiceStateView.setCurrentState(CircleVoiceStateView.State.ACTIVE_LISTENING);
                                }
                            }
                        });
                    }
                    if (sink != null && recorder != null) {
                        sink.write(recorder.consumeRecording());
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
            mVoiceStateView.post(new Runnable() {
                @Override
                public void run() {
                    stateProcessing();
                }
            });
            stopListening();
        }

    };


    @Override
    protected void onDestroy() {
        if (alexaReceiver != null) {
            unregisterReceiver(alexaReceiver);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void fadeOutView() {
        EventBus.getDefault().post("");
        Logger.d("fadeOutView: --");
//        finish();
        overridePendingTransition(0,R.animator.out_activity);
        /*AlphaAnimation animation = new AlphaAnimation(1, 0);
        animation.setDuration(400);
        animation.setFillAfter(true);
        mVoiceStateView.setAnimation(animation);
        animation.start();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "onAnimationEnd: Sending PlaybackFinishedEvent");
//                finish();
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        //execute the task
                        Log.d(TAG, "run: Sending PlaybackFinishedEvent finish");
                        finish();
                    }
                }, 300);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/
    }

    @Override
    protected void startListening() {
        Log.d(TAG, "------------> startListening");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.LISTENING);
        if (recorder == null) {
            Log.d(TAG, "startListening: recorder = null");
            recorder = new RawAudioRecorder(AUDIO_RATE);
            recorder.start();
            alexaManager.sendAudioRequest(requestBody, getRequestCallback());
        }
    }

    @Override
    protected void stateListening() {
        Log.d(TAG, "------------> stateListening");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.ACTIVE_LISTENING);
    }

    @Override
    protected void stateProcessing() {
        Log.d(TAG, "------------> stateProcessing");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.THINKING);
    }

    @Override
    protected void stateSpeaking() {
        Log.d(TAG, "------------> stateSpeaking");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.SPEAKING);
    }

    @Override
    protected void stateFinished() {
        Log.d(TAG, "------------> stateFinished");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.IDLE);
    }

    @Override
    protected void statePrompting() {
        Log.d(TAG, "------------> statePrompting");
    }

    @Override
    protected void stateNone() {
        Log.d(TAG, "------------> stateNone");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.IDLE);
    }

    private void stopListening() {
        Log.d(TAG, "------------> stopListening");
        if (recorder != null) {
//            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    public class AlexaReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "onReceive: intent is " + intent.getAction() + "==" + ResponseParser.RECOGNIZE_STATE);
            if (Objects.equals(intent.getAction(), "com.konka.android.intent.action.START_VOICE") /*&& !ResponseParser.RECOGNIZE_STATE*/) {
                startListening();
            }
            if (Objects.equals(intent.getAction(), "com.konka.android.intent.action.STOP_VOICE")) {
                stopListening();
            }
        }
    }
}
