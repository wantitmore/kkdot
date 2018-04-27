package com.willblaschko.android.alexavoicelibrary.display;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.willblaschko.android.alexa.audioplayer.AlexaAudioPlayer;
import com.willblaschko.android.alexa.beans.PlayerInfoBean;
import com.willblaschko.android.alexa.interfaces.AvsItem;
import com.willblaschko.android.alexa.interfaces.audioplayer.AvsPlayRemoteItem;
import com.willblaschko.android.alexavoicelibrary.R;
import com.willblaschko.android.alexavoicelibrary.utility.TimeFormatUitls;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayInfoActivity extends AppCompatActivity  implements MusicProgressCallBack{

    private static final String TAG = "PlayInfoActivity";
    private int mDuration;
    private ProgressBar mProgressBar;
    private TextView mDurationView;
    private TextView mCurrentTimeView;
    private LinearLayout mProgressViewContainer;
    TextView mMainHeaderView;
    TextView mHeaderSubtext;
    TextView mMainTitleView;
    TextView mTitleSubtext1;
    TextView mTitleSubtext2;
    ImageView mLogoView;
    ImageView mArtView;
    LinearLayout mControlContainer;

    private DisplayService.DisplayBinder mBinder;

//    private MusicProgressCallBack mMusicProgressCallBack;


    public final static Map<String, Integer> map = new LinkedHashMap<String, Integer>() {{
        put("PREVIOUS", R.drawable.play_back_controller_previous);
        put("PLAY_PAUSE", R.drawable.play_back_controller_pause);
        put("NEXT", R.drawable.play_back_controller_next);
    }};

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: -------------");
            mBinder = (DisplayService.DisplayBinder) service;
            mBinder.setMusicProgressCallBack(PlayInfoActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private PlayerInfoBean renderObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_player_info);


        Intent intent = new Intent(this, DisplayService.class);
        intent.putExtra("PlayTag", true);
        bindService(intent, conn, BIND_AUTO_CREATE);
        Log.d(TAG, "onResume: mBinder is " + mBinder);

        Intent displayIntent = getIntent();
        Bundle bundle = displayIntent.getExtras();
        if (bundle != null) {
            renderObj = bundle.getParcelable("args");
        }

        initView();

        refreshUI(renderObj);
    }

    private void initView() {
        mMainHeaderView = (TextView) findViewById(R.id.main_header);
        mHeaderSubtext = (TextView) findViewById(R.id.sub_header);
        mMainTitleView = (TextView) findViewById(R.id.main_title);
        mTitleSubtext1 = (TextView) findViewById(R.id.title_subtext1);
        mTitleSubtext2 = (TextView) findViewById(R.id.title_subtext2);
        mLogoView = (ImageView) findViewById(R.id.logo_img);
        mArtView = (ImageView) findViewById(R.id.art_view);
        mControlContainer = (LinearLayout) findViewById(R.id.control_container);
        mProgressBar = (ProgressBar) findViewById(R.id.seek_bar);
        mCurrentTimeView = (TextView) findViewById(R.id.current_time);
        mDurationView = (TextView) findViewById(R.id.duration);
        mProgressViewContainer = (LinearLayout) findViewById(R.id.progress_container);
        mProgressViewContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(Object renderObj) {
        if (renderObj instanceof PlayerInfoBean) {
            refreshUI((PlayerInfoBean) renderObj);
        }
    }

    @Override
    public void onProgressChange(final AvsItem item, final long offsetInMilliseconds, final long duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDuration = (int) duration;
                if (item instanceof AvsPlayRemoteItem && mDuration != 0) {
                    mProgressViewContainer.setVisibility(View.VISIBLE);
                    mProgressBar.setMax(mDuration);
                    mProgressBar.setProgress((int) offsetInMilliseconds);
                    final String strCurrentTime = TimeFormatUitls.formatTime((int) offsetInMilliseconds);
                    final String strDuration = TimeFormatUitls.formatTime(mDuration);
                    mCurrentTimeView.setText(strCurrentTime);
                    mDurationView.setText(strDuration);
                } else {
                    mProgressViewContainer.setVisibility(View.GONE);
                }
            }
        });
    }

/*    public void sendPlaybackControllerPauseCommandIssued() {
        alexaManager.sendPlaybackControllerPauseCommandIssued(requestCallback);
    }

    public void sendPlaybackControllerPlayCommandIssued() {
        alexaManager.sendPlaybackControllerPlayCommandIssued(requestCallback);
    }*/



    public void refreshUI(PlayerInfoBean renderObj){

        PlayerInfoBean.DirectiveBean.PayloadBean payloadBean = renderObj.getDirective().getPayload();
        PlayerInfoBean.DirectiveBean.PayloadBean.ContentBean contentBean = payloadBean.getContent();
        final String headerText = contentBean.getHeader();
        final String headerSubtext = contentBean.getHeaderSubtext1();
        final String title = contentBean.getTitle();
        final String titleSubtext1 = contentBean.getTitleSubtext1();
        final String titleSubtext2 = contentBean.getTitleSubtext2();
        mDuration =  contentBean.getMediaLengthInMilliseconds();
        int artUrlSize = contentBean.getArt().getSources().size();
        String artUrl;
        Log.d(TAG,"artUrlSize:"+artUrlSize);
        if(artUrlSize > 1) {
            int artSize = 0;// x-small:1,small :2,medium:3,large:4,x-large:5
            int location = 0;
            String sourceSize;
            for(int i = 0; i < artUrlSize;i++){
                sourceSize = contentBean.getArt().getSources().get(i).getSize();
                if(sourceSize == null){
                    continue;
                }

                if(sourceSize.equalsIgnoreCase("x-large")) {
                    artSize = 5;
                    location =i;
                }
                if(sourceSize.equalsIgnoreCase("large") && artSize<4) {
                    artSize = 4;
                    location =i;
                }
                if(sourceSize.equalsIgnoreCase("medium") && artSize<3) {
                    artSize = 3;
                    location =i;
                }

                if(sourceSize.equalsIgnoreCase("small") && artSize<2) {
                    artSize = 2;
                    location =i;
                }

                if(sourceSize.equalsIgnoreCase("small") && artSize<1) {
                    artSize = 1;
                    location =i;
                }
            }

            artUrl = contentBean.getArt().getSources().get(location).getUrl();
            Log.d(TAG,"artUrl:"+artUrl+",location"+location);
        }else{
            artUrl = contentBean.getArt().getSources().get(0).getUrl();
        }

        final String logoUrl = contentBean.getProvider().getLogo().getSources().get(0).getUrl();;

        mMainHeaderView.setText(headerText);
        mHeaderSubtext.setText(headerSubtext);
        mMainTitleView.setText(title);
        mTitleSubtext1.setText(titleSubtext1);
        mTitleSubtext2.setText(titleSubtext2);
        Glide.with(this).load(logoUrl).into(mLogoView);
        Glide.with(this).load(artUrl).into(mArtView);
        mControlContainer.removeAllViews();

        final List<PlayerInfoBean.DirectiveBean.PayloadBean.ControlsBean> controlsDatas = payloadBean.getControls();

        if (controlsDatas != null && !controlsDatas.isEmpty()) {
            List<PlayerInfoBean.DirectiveBean.PayloadBean.ControlsBean> targetBeans = new ArrayList<>();
            final Set<Map.Entry<String, Integer>> entries = map.entrySet();
            for (Map.Entry entry: entries) {
                String key = (String) entry.getKey();
                Log.d(TAG, "key = " + key);
                for (PlayerInfoBean.DirectiveBean.PayloadBean.ControlsBean item : controlsDatas) {
                    String name = item.getName();
                    if (name.equals(key)) {
                        targetBeans.add(item);
                    }
                }
            }

            int size = targetBeans.size();
            for (int i = 0; i < size; i++) {
                PlayerInfoBean.DirectiveBean.PayloadBean.ControlsBean item = targetBeans.get(i);
                final String name = item.getName();
                Integer resId = map.get(name);
                if (resId != null) {
                    final ImageView iv = new ImageView(this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.leftMargin = 30;
                    lp.rightMargin = 30;
                    iv.setTag(name);
                    iv.setFocusable(true);
                    if (!AlexaAudioPlayer.getInstance(this).isPlaying() && ("PLAY_PAUSE".equals(name))) {
                        Log.d(TAG, "refreshUI: -------------not Playing");
                        iv.setImageResource(R.drawable.play_back_controller_pause);
                    } else {
                        iv.setImageResource(resId);
                    }
                    iv.setLayoutParams(lp);

                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (name) {
                                case "PLAY_PAUSE":
                                    Log.d(TAG, "onClick: -----------------");
                                    playControl(iv);

                                    break;
                                case "PREVIOUS":
                                    mBinder.sendPlaybackControllerPreviousCommandIssued();
                                    break;
                                case "NEXT":
                                    mBinder.sendPlaybackControllerNextCommandIssued();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });

                    mControlContainer.addView(iv);
                }
            }
        }
    }

    private void playControl(ImageView iv) {
        if (AlexaAudioPlayer.getInstance(this).isPlaying()) {
            Log.d(TAG, "playControl: play_back_controller_play");
            mBinder.sendPlaybackControllerPauseCommandIssued();
            iv.setImageResource(R.drawable.play_back_controller_play);
        } else {
            Log.d(TAG, "playControl: play_back_controller_pause");
            mBinder.sendPlaybackControllerPlayCommandIssued();
            iv.setImageResource(R.drawable.play_back_controller_pause);
        }
    }
}
