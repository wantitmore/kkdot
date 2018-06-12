package com.konka.alexa.display;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.konka.alexa.alexalib.GlideApp;
import com.konka.alexa.alexalib.audioplayer.AlexaAudioPlayer;
import com.konka.alexa.alexalib.beans.PlayerInfoBean;
import com.konka.alexa.alexalib.interfaces.AvsItem;
import com.konka.alexa.alexalib.interfaces.audioplayer.AvsPlayRemoteItem;
import com.konka.alexa.alexalib.system.AndroidSystemHandler;
import com.konka.alexa.alexalib.utility.svg.SvgSoftwareLayerSetter;
import com.konka.alexa.R;
import com.konka.alexa.utility.TimeFormatUitls;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PlayInfoActivity extends AppCompatActivity  implements com.konka.alexa.display.MusicProgressCallBack {

    private static final String TAG = "PlayInfoActivity";
    private int mDuration;
    private ProgressBar mProgressBar;
    private TextView mDurationView;
    private TextView mCurrentTimeView;
    private LinearLayout mProgressViewContainer;
    private Boolean mAcceptEventBusEvent;
    TextView mMainHeaderView;
    TextView mHeaderSubtext;
    TextView mMainTitleView;
    TextView mTitleSubtext1;
    TextView mTitleSubtext2;
    ImageView mLogoView;
    ImageView mArtView;
    LinearLayout mControlContainer;
    private String tag;
    private RequestBuilder<PictureDrawable> requestBuilder;
    public static int isTop = 0;
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
    private LinearLayout mControlOnly;
    private ImageView mLeft;
    private ImageView mRight;
    private ImageView mPlay;
    private LinearLayout mHeadLayout;
    private LinearLayout mContentLayout;
    private LinearLayout mPlayInfo;
    private AlexaAudioPlayer mAudioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.fragment_player_info);
        EventBus.getDefault().register(this);
        mAudioPlayer = AlexaAudioPlayer.getInstance(this);
        Intent intent = new Intent(this, DisplayService.class);
        intent.putExtra("PlayTag", true);
        bindService(intent, conn, BIND_AUTO_CREATE);
        Log.d(TAG, "onCreate: mBinder is " + mBinder);

        initView();
        initListener();
        if (bundle != null) {
            mPlayInfo.setBackgroundColor(Color.parseColor("#000000"));
            renderObj = bundle.getParcelable("args");
            refreshUI(renderObj);
            mControlOnly.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            mHeadLayout.setVisibility(View.VISIBLE);
            mProgressViewContainer.setVisibility(View.GONE);
        } else {
            mControlOnly.setVisibility(View.VISIBLE);
            mContentLayout.setVisibility(View.GONE);
            mHeadLayout.setVisibility(View.GONE);
            mPlay.setTag("PLAY_PAUSE");
        }

    }

    @Override
    protected void onResume() {

        Log.d(TAG, "onResume: ");
        mAcceptEventBusEvent = true;
        isTop = 1;
        super.onResume();
    }

    @Override
    protected void onPause(){
        Log.i(TAG,"onPause");
        if (mBinder != null) {
            mBinder.setAcceptEventBusEvent(false);
        }
        mAcceptEventBusEvent = false;
        isTop = 0;
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mBinder.stop();
        finish();
    }


    @Override
    protected void onDestroy() {
        Log.i(TAG,"onDestroy");
        unbindService(conn);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private void initListener() {
        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click left");
                mBinder.sendPlaybackControllerPreviousCommandIssued();
            }
        });
        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click right");
                mBinder.sendPlaybackControllerNextCommandIssued();
            }
        });
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click play");
                playControl(mPlay);
            }
        });
        AndroidSystemHandler.getInstance(this).setmOnAudioPlayerListener(new AndroidSystemHandler.OnAudioPlayerListener() {
            @Override
            public void stop() {
                Log.d(TAG, "stop AudioPlayer from cloud");
                if(mAudioPlayer.isPlaying()){
                    if(mAudioPlayer.getCurrentItem() instanceof AvsPlayRemoteItem){
                        ((AvsPlayRemoteItem) mAudioPlayer.getCurrentItem()).SetNeedResumed(true);
                        mBinder.sendPlaybackStopEvent();
                        mAudioPlayer.stop();
                    }
                }
            }
        });
    }

    private void initView() {
        mPlayInfo = (LinearLayout) findViewById(R.id.ll_play_info);
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
        mControlOnly = (LinearLayout) findViewById(R.id.ll_control_only);
        mLeft = (ImageView) findViewById(R.id.left);
        mRight = (ImageView) findViewById(R.id.right);
        mPlay = (ImageView) findViewById(R.id.play);
        mHeadLayout = (LinearLayout) findViewById(R.id.ll_header);
        mContentLayout = (LinearLayout) findViewById(R.id.ll_main_content);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(Object renderObj) {
        Log.d(TAG, "handleEvent: ");
        if(!mAcceptEventBusEvent){
            return;
        }

        if (renderObj instanceof PlayerInfoBean) {
            mPlayInfo.setBackgroundColor(Color.parseColor("#000000"));
            mControlOnly.setVisibility(View.GONE);
            mHeadLayout.setVisibility(View.VISIBLE);
            mContentLayout.setVisibility(View.VISIBLE);
            refreshUI((PlayerInfoBean) renderObj);
        } else if (renderObj instanceof String && TextUtils.equals((String)renderObj, "Controls-Only")) {
            Log.d(TAG, "handleEvent: Controls-Only");
            mPlay.setTag("PLAY_PAUSE");
            mPlay.setImageResource(R.drawable.play_back_controller_pause);
            mPlayInfo.setBackgroundColor(Color.parseColor("#D9000000"));
            mControlOnly.setVisibility(View.VISIBLE);
            mHeadLayout.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPlaystateChange(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    ImageView iv;
                    if (!(mControlOnly.getVisibility() == View.VISIBLE)) {
                        iv = (ImageView) mControlContainer.findViewWithTag("PLAY_PAUSE");
                    } else {
                        iv = mPlay;
                    }

                    if (AlexaAudioPlayer.getInstance(PlayInfoActivity.this).isPlaying()) {
                        iv.setImageResource(R.drawable.play_back_controller_pause);
                    } else {
                        iv.setImageResource(R.drawable.play_back_controller_play);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onProgressChange(final AvsItem item, final long offsetInMilliseconds, final long duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (item instanceof AvsPlayRemoteItem && mDuration != 0) {
                    mProgressViewContainer.setVisibility(View.VISIBLE);
                    mProgressBar.setMax(mDuration);
                    mProgressBar.setProgress((int) offsetInMilliseconds);
                    final String strCurrentTime = TimeFormatUitls.formatTime((int) offsetInMilliseconds);
                    final String strDuration = TimeFormatUitls.formatTime(mDuration);
                    mCurrentTimeView.setText(strCurrentTime);
                    mDurationView.setText(strDuration);
                } else if (mDuration <= 0)  {
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
        requestBuilder = GlideApp.with(this)
                .as(PictureDrawable.class)
//                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.alexa_logo)
                .transition(withCrossFade())
                .listener(new SvgSoftwareLayerSetter());
        requestBuilder.load(logoUrl).into(mLogoView);

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
                    if (name.equals(tag)) {
                        Log.d(TAG, "refreshUI: -----");
                        iv.setFocusableInTouchMode(true);
                        iv.requestFocus();
                    }
                    if (!AlexaAudioPlayer.getInstance(this).isPlaying() && ("PLAY_PAUSE".equals(name))) {
                        Log.d(TAG, "refreshUI: -------------not Playing");
                        iv.setImageResource(R.drawable.play_back_controller_pause);
                    } else {
                        iv.setImageResource(resId);
                    }
                    iv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                Log.d(TAG, "onFocusChange: focus is " + name);
                                tag = name;
                            }
                        }
                    });
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
