package com.willblaschko.android.alexavoicelibrary.display;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.willblaschko.android.alexa.beans.WeatherTemplateBean;
import com.willblaschko.android.alexavoicelibrary.R;

import java.util.List;


public class WeatherTemplateFragment extends Fragment {

    public static WeatherTemplateFragment newInstance() {
        return new WeatherTemplateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        WeatherTemplateBean renderObj = args.getParcelable("args");

        View view = inflater.inflate(R.layout.fragment_weather_template, container, false);

        TextView mainTitleView = (TextView) view.findViewById(R.id.main_title);
        TextView subTitleView = (TextView) view.findViewById(R.id.sub_title);
        ImageView currentWeatherImgView = (ImageView) view.findViewById(R.id.today_img);
        TextView currentWeatherTv = (TextView) view.findViewById(R.id.current_weather);

        WeatherTemplateBean.DirectiveBean.PayloadBean payloadBean = renderObj.getDirective().getPayload();
        WeatherTemplateBean.DirectiveBean.PayloadBean.TitleBean titleBean = payloadBean.getTitle();
        mainTitleView.setText(titleBean.getMainTitle());
        subTitleView.setText(titleBean.getSubTitle());

        List<WeatherTemplateBean.DirectiveBean.PayloadBean.CurrentWeatherIconBean.SourcesBeanX> currentWeatherSources = payloadBean.getCurrentWeatherIcon().getSources();
        String currentWeatherIconUrl = currentWeatherSources.get(currentWeatherSources.size() - 1).getUrl();
        Glide.with(getActivity()).load(currentWeatherIconUrl).into(currentWeatherImgView);
        currentWeatherTv.setText(payloadBean.getCurrentWeather());

        return view;
    }
}
