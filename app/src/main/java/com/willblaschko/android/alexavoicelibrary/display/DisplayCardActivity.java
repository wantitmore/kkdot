package com.willblaschko.android.alexavoicelibrary.display;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.beans.ListTemplate1Bean;
import com.willblaschko.android.alexa.beans.Template1Bean;
import com.willblaschko.android.alexa.beans.Template2Bean;
import com.willblaschko.android.alexa.beans.WeatherTemplateBean;
import com.willblaschko.android.alexavoicelibrary.BaseActivity;
import com.willblaschko.android.alexavoicelibrary.R;
import com.willblaschko.android.alexavoicelibrary.widget.CircleVoiceStateView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.willblaschko.android.alexavoicelibrary.global.Constants.PRODUCT_ID;


public class DisplayCardActivity extends BaseActivity {
    private final static String TAG = DisplayCardActivity.class.getSimpleName();
    private CircleVoiceStateView mVoiceStateView;
    private EditText search;
    private View button;
    private AlexaManager alexaManager;
    private Fragment mShowingFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        /*Intent intent = getIntent();
        Object args = intent.getParcelableExtra("args");
        if (args instanceof Template1Bean) {

        } else if (args instanceof Template2Bean) {

            Log.d(TAG, "Template2Bean: " + ((Template2Bean) args).getDirective().getPayload().getTitle().getMainTitle());
        } else if (args instanceof WeatherTemplateBean) {

            Log.d(TAG, "WeatherTemplateBean: " + ((WeatherTemplateBean) args).getDirective().getPayload().getTitle().getMainTitle());
        }*/
    }


    private void search(){
        String text = search.getText().toString();
        alexaManager.sendTextRequest(text, getRequestCallback());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(Object renderObj) {

        Bundle args = new Bundle();
        if (renderObj instanceof Template1Bean) {
            Log.d(DisplayCardActivity.class.getSimpleName(), "app Template1Bean = " + renderObj.toString());
            mShowingFragment = Template1Fragment.newInstance();
            args.putParcelable("args", (Parcelable) renderObj);
            mShowingFragment.setArguments(args);
        } else if (renderObj instanceof Template2Bean) {
            Log.d(DisplayCardActivity.class.getSimpleName(), "app Template2Bean = " + renderObj.toString());
            mShowingFragment = Template2Fragment.newInstance();
            args.putParcelable("args", (Parcelable) renderObj);
            mShowingFragment.setArguments(args);
        } else if (renderObj instanceof WeatherTemplateBean) {
            Log.d(DisplayCardActivity.class.getSimpleName(), "app weather = " + renderObj.toString());
            mShowingFragment = WeatherTemplateFragment.newInstance();
            args.putParcelable("args", (Parcelable) renderObj);
            mShowingFragment.setArguments(args);
        } else if (renderObj instanceof ListTemplate1Bean) {
            Log.d(DisplayCardActivity.class.getSimpleName(), "app ListTemplate1Bean = " + renderObj.toString());
            mShowingFragment = ListTemplate1Fragment.newInstance();
            args.putParcelable("args", (Parcelable) renderObj);
            mShowingFragment.setArguments(args);
        } else {
            mShowingFragment = EmptyFragment.newInstance();
        }
        mFragmentManager.beginTransaction().replace(R.id.main_display_content, mShowingFragment).commit();
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void startListening() {
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.LISTENING);
    }

    @Override
    protected void stateListening() {
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.ACTIVE_LISTENING);
    }

    @Override
    protected void stateProcessing() {
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.THINKING);
    }

    @Override
    protected void stateSpeaking() {
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.SPEAKING);
    }

    @Override
    protected void stateFinished() {
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.IDLE);
    }

    @Override
    protected void statePrompting() {

    }

    @Override
    protected void stateNone() {
        mVoiceStateView.setCurrentState(CircleVoiceStateView.State.IDLE);
    }
}
