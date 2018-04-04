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
    private boolean isPlaying = true;
    private ProgressBar mProgressBar;
    private TextView mDurationView;
    private TextView mCurrentTimeView;
    private LinearLayout mProgressViewContainer;

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
        TextView mainHeaderView = (TextView) view.findViewById(R.id.main_header);
        TextView mainTitleView = (TextView) view.findViewById(R.id.main_title);
        ImageView logoView = (ImageView) view.findViewById(R.id.logo_img);
        ImageView artView = (ImageView) view.findViewById(R.id.art_view);
        LinearLayout controlContainer = (LinearLayout) view.findViewById(R.id.control_container);
        mProgressBar = (ProgressBar) view.findViewById(R.id.seek_bar);
        mCurrentTimeView = (TextView) view.findViewById(R.id.current_time);
        mDurationView = (TextView) view.findViewById(R.id.duration);
        mProgressViewContainer = (LinearLayout) view.findViewById(R.id.progress_container);
        mProgressViewContainer.setVisibility(View.GONE);

        PlayerInfoBean.DirectiveBean.PayloadBean payloadBean = renderObj.getDirective().getPayload();
        PlayerInfoBean.DirectiveBean.PayloadBean.ContentBean contentBean = payloadBean.getContent();
        final String headerText = contentBean.getHeader();
        final String title = contentBean.getTitle();
        final String logoUrl = contentBean.getProvider().getLogo().getSources().get(0).getUrl();
        final String artUrl = contentBean.getArt().getSources().get(0).getUrl();
        mainHeaderView.setText(headerText);
        mainTitleView.setText(title);
        Glide.with(getActivity()).load(logoUrl).into(logoView);
        Glide.with(getActivity()).load(artUrl).into(artView);

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
                    iv.setImageResource(resId);
                    iv.setLayoutParams(lp);

                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (name) {
                                case "PLAY_PAUSE":
                                    if (isPlaying) {
                                        mActivity.sendPlaybackControllerPauseCommandIssued();
                                        iv.setImageResource(R.drawable.play_back_controller_play);
                                    } else {
                                        mActivity.sendPlaybackControllerPlayCommandIssued();
                                        iv.setImageResource(R.drawable.play_back_controller_pause);
                                    }
                                    isPlaying = !isPlaying;

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

                    controlContainer.addView(iv);
                }
            }
        }

        return view;
    }

    @Override
    public void onProgressChange(final AvsItem item, final long offsetInMilliseconds, final float percent) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (item instanceof AvsPlayRemoteItem) {
                    mProgressViewContainer.setVisibility(View.VISIBLE);
                    final int duration = (int) (offsetInMilliseconds / percent);
                    mProgressBar.setMax(duration);
                    mProgressBar.setProgress((int) offsetInMilliseconds);
                    final String strCurrentTime = TimeFormatUitls.formatTime((int) offsetInMilliseconds);
                    final String strDuration = TimeFormatUitls.formatTime(duration);
                    mCurrentTimeView.setText(strCurrentTime);
                    mDurationView.setText(strDuration);
                } else {
                    mProgressViewContainer.setVisibility(View.GONE);
                }
            }
        });
    }
}