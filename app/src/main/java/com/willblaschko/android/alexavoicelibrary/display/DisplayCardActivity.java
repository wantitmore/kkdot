package com.willblaschko.android.alexavoicelibrary.display;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
    private CircleVoiceStateView mVoiceStateView;
    private EditText search;
    private View button;
    private AlexaManager alexaManager;
    private Fragment mShowingFragment;
    private FragmentManager mFragmentManager;
    private RawAudioRecorder recorder;
    private AlexaReceiver alexaReceiver;

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

//        WindowManager windowManager = getWindowManager();
//        DisplayMetrics dm = new DisplayMetrics();
//        windowManager.getDefaultDisplay().getMetrics(dm);
//        mScreenWidth = dm.widthPixels;
//        mScreenHeight = dm.heightPixels;

        alexaManager = AlexaManager.getInstance(this, PRODUCT_ID);

        setContentView(R.layout.activity_display_card);
        mVoiceStateView = (CircleVoiceStateView) findViewById(R.id.voice_view_state);

        EventBus.getDefault().register(this);

        mFragmentManager = getFragmentManager();

        search = (EditText) findViewById(R.id.search);
        button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

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


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void search(){
        String text = search.getText().toString();
        alexaManager.sendTextRequest(text, getRequestCallback());
    }

 /*   @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && mShowingFragment instanceof PlayerInfoFragment) {

            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                Log.d(TAG, "onKeyUp: enter--------");
                onPlayControlListener.onPlayControl();
            } else if ((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT)) {
                Log.d(TAG, "onKeyUp: left------");
                onPlayControlListener.onPreControl();

            } else if ((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT)) {
                Log.d(TAG, "onKeyUp: right------");
                onPlayControlListener.onNextControl();
            }
        }
        return super.dispatchKeyEvent(event);
    }*/

    public interface OnPlayControlListener {
        void onPlayControl();
        void onPreControl();
        void onNextControl();
    }

    public OnPlayControlListener onPlayControlListener;

    public void setOnPlayControlListener(OnPlayControlListener onPlayControlListener) {
        this.onPlayControlListener = onPlayControlListener;
    }


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

            if (mShowingFragment instanceof PlayerInfoFragment) {
                Log.d(TAG,"just refresh ui,not create new instance");
                ((PlayerInfoFragment) mShowingFragment).refreshUI((PlayerInfoBean) renderObj);
                return;
            }

            mShowingFragment = PlayerInfoFragment.newInstance();
        } else if (!renderObj.equals("SpeakEnd") && (!renderObj.equals("SpeakStart"))) {
            mShowingFragment = EmptyFragment.newInstance();
        }
        if (renderObj instanceof Parcelable) {
            args.putParcelable("args", (Parcelable) renderObj);
            mShowingFragment.setArguments(args);
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.setCustomAnimations()
        transaction.setCustomAnimations(R.animator.enter, R.animator.out).addToBackStack(null);
        transaction.replace(R.id.main_display_content, mShowingFragment).commitAllowingStateLoss();
     }


    private DataRequestBody requestBody = new DataRequestBody() {
        @Override
        public void writeTo(BufferedSink sink) throws IOException {
//            Log.d(TAG, "writeTo: recorder is " + recorder + ", is Pa");
            boolean changeState = true;
//            Log.i(TAG,"will enter writeTo while loop");
            while (recorder != null && !recorder.isPausing()) {
                try {
//                    Log.d(TAG, "writeTo: record is null? " + recorder);
                    if (recorder != null) {
                        final float rmsdb = recorder.getRmsdb();
//                        Log.d(TAG, "run: ----rmsdb is " + rmsdb + ",recorder.isPausing():" + recorder.isPausing());
                        CircleVoiceStateView.State currentState = mVoiceStateView.getCurrentState();

                        if (changeState && rmsdb != 0) {
                            mVoiceStateView.post(new Runnable() {
                                @Override
                                public void run() {
                                        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.ACTIVE_LISTENING);
                                    }
                                });
                            changeState =false;
                        }
                    }

                    if (sink != null && recorder != null) {
                        sink.write(recorder.consumeRecording());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "writeTo: error is " + e.getMessage());
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            Log.i(TAG,"exit writeTo while loop");
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
        Log.d(TAG, "fadeOutView: --");
//         finish();
        overridePendingTransition(0,R.animator.out_activity);
    }

    protected void startListening() {
        Log.d(TAG, "------------> startListening");
        //recorder is not null,mean that an audio request is sentting
        if (recorder == null) {
            mVoiceStateView.setCurrentState(CircleVoiceStateView.State.LISTENING);
            Log.d(TAG, "startListening: recorder = null");
            recorder = new RawAudioRecorder(AUDIO_RATE);
            recorder.start();
            alexaManager.sendAudioRequest(requestBody, getRequestCallback());
            stopCurrentPlayingItem();
        }

    }

    protected void stateListening() {
        Log.d(TAG, "------------> stateListening");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.ACTIVE_LISTENING);
    }

    protected void stateProcessing() {
        Log.d(TAG, "------------> stateProcessing");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.THINKING);
    }

    protected void stateSpeaking() {
        Log.d(TAG, "------------> stateSpeaking");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.SPEAKING);
    }

    protected void stateFinished() {
        Log.d(TAG, "------------> stateFinished");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.IDLE);
    }

    protected void statePrompting() {
        Log.d(TAG, "------------> statePrompting");
    }

    protected void stateNone() {
        Log.d(TAG, "------------> stateNone");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.IDLE);
    }

    protected void  stateError(){
        Log.d(TAG, "------------> stateError");
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.SYSTEM_ERR);
    }

    private void stopListening() {
        Log.d(TAG, "------------> stopListening");
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    public class AlexaReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "onReceive: intent is " + intent.getAction() + "--" + ResponseParser.RECOGNIZE_STATE);
            if (Objects.equals(intent.getAction(), "com.konka.android.intent.action.START_VOICE")/* && !ResponseParser.RECOGNIZE_STATE*/) {
                startListening();
            }
            if (Objects.equals(intent.getAction(), "com.konka.android.intent.action.STOP_VOICE")) {
                stopListening();
            }
        }
    }
}
