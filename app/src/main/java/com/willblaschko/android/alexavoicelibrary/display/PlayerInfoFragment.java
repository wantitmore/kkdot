package com.willblaschko.android.alexavoicelibrary.display;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PlayerInfoFragment extends Fragment implements MusicProgressCallBack {

    private static final String TAG = PlayerInfoFragment.class.getSimpleName();
    private DisplayCardActivity mActivity;
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


    public final static Map<String, Integer> map = new LinkedHashMap<String, Integer>() {{
        put("PREVIOUS", R.drawable.play_back_controller_previous);
        put("PLAY_PAUSE", R.drawable.play_back_controller_pause);
        put("NEXT", R.drawable.play_back_controller_next);
    }};


    public static PlayerInfoFragment newInstance() {
        return new PlayerInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mActivity = (DisplayCardActivity) getActivity();

        mActivity.setMusicProgressCallBack(this);

        Bundle args = getArguments();
        PlayerInfoBean renderObj = args.getParcelable("args");

        View view = inflater.inflate(R.layout.fragment_player_info, container, false);
        mMainHeaderView = (TextView) view.findViewById(R.id.main_header);
        mHeaderSubtext = (TextView) view.findViewById(R.id.sub_header);
        mMainTitleView = (TextView) view.findViewById(R.id.main_title);
        mTitleSubtext1 = (TextView) view.findViewById(R.id.title_subtext1);
        mTitleSubtext2 = (TextView) view.findViewById(R.id.title_subtext2);
        mLogoView = (ImageView) view.findViewById(R.id.logo_img);
        mArtView = (ImageView) view.findViewById(R.id.art_view);
        mControlContainer = (LinearLayout) view.findViewById(R.id.control_container);
        mProgressBar = (ProgressBar) view.findViewById(R.id.seek_bar);
        mCurrentTimeView = (TextView) view.findViewById(R.id.current_time);
        mDurationView = (TextView) view.findViewById(R.id.duration);
        mProgressViewContainer = (LinearLayout) view.findViewById(R.id.progress_container);
        mProgressViewContainer.setVisibility(View.GONE);

        refreshUI(renderObj);
        return view;
    }

    @Override
    public void onProgressChange(final AvsItem item, final long offsetInMilliseconds, final long duration) {
        mActivity.runOnUiThread(new Runnable() {
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

    @Override
    public void onPlaystateChange(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ImageView iv = (ImageView) mControlContainer.findViewWithTag("PLAY_PAUSE");
                    if (AlexaAudioPlayer.getInstance(getActivity()).isPlaying()) {
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
        Glide.with(getActivity()).load(logoUrl).into(mLogoView);
        Glide.with(getActivity()).load(artUrl).into(mArtView);
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
                    final ImageView iv = new ImageView(getActivity());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.leftMargin = 30;
                    lp.rightMargin = 30;
                    iv.setTag(name);
                    iv.setFocusable(true);
                    if (!AlexaAudioPlayer.getInstance(getActivity()).isPlaying() && ("PLAY_PAUSE".equals(name))) {
                        Log.d(TAG, "refreshUI: -------------not Playing");
                        iv.setImageResource(R.drawable.play_back_controller_pause);
                    } else {
                        iv.setImageResource(resId);
                    }
                    iv.setLayoutParams(lp);

                    /*mActivity.setOnPlayControlListener(new DisplayCardActivity.OnPlayControlListener() {
                        @Override
                        public void onPlayControl() {
                            for (int i = 0;i < mControlContainer.getChildCount(); i++) {
                                ImageView img = (ImageView) mControlContainer.getChildAt(i);
                                String tag = (String) img.getTag();
                                Log.d(TAG, "onPlayControl: --"  + "  " + tag);
                                if ("PLAY_PAUSE".equals(tag)) {
                                    playControl(img);
                                }
                            }

                        }

                        @Override
                        public void onPreControl() {
                            mActivity.sendPlaybackControllerPreviousCommandIssued();
                        }

                        @Override
                        public void onNextControl() {
                            mActivity.sendPlaybackControllerNextCommandIssued();
                        }
                    });*/

                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (name) {
                                case "PLAY_PAUSE":
                                    Log.d(TAG, "onClick: -----------------");
                                    playControl(iv);

                                    break;
                                case "PREVIOUS":
                                    mActivity.sendPlaybackControllerPreviousCommandIssued();
                                    break;
                                case "NEXT":
                                    mActivity.sendPlaybackControllerNextCommandIssued();
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
        if (AlexaAudioPlayer.getInstance(getActivity()).isPlaying()) {
            Log.d(TAG, "playControl: play_back_controller_play");
            mActivity.sendPlaybackControllerPauseCommandIssued();
            iv.setImageResource(R.drawable.play_back_controller_play);
        } else {
            Log.d(TAG, "playControl: play_back_controller_pause");
            mActivity.sendPlaybackControllerPlayCommandIssued();
            iv.setImageResource(R.drawable.play_back_controller_pause);
        }
    }
}
