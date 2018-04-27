package com.willblaschko.android.alexavoicelibrary.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.willblaschko.android.alexa.beans.ListTemplate1Bean;
import com.willblaschko.android.alexa.beans.Template1Bean;
import com.willblaschko.android.alexa.beans.Template2Bean;
import com.willblaschko.android.alexa.beans.WeatherTemplateBean;
import com.willblaschko.android.alexavoicelibrary.R;

import java.util.List;

/**
 * Created by user001 on 2018-4-26.
 */

public class ContentView {

    private static final String TAG = "ContentView";



    public View setView(Context context, Object content) {
        View view = null;
        if (content instanceof Template1Bean) {
            view = setTem1View(context, (Template1Bean) content);
        } else if (content instanceof Template2Bean) {
            view = setTem2View(context, (Template2Bean) content);
        } else if (content instanceof WeatherTemplateBean) {
            view = setWeatherView(context, (WeatherTemplateBean) content);
        } else if (content instanceof ListTemplate1Bean) {

            view = setListTemView(context, (ListTemplate1Bean) content);
        }
        return view;
    }

    @NonNull
    private View setListTemView(Context context, ListTemplate1Bean content) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.fragment_listtemplate1, null);

        TextView mainTitleView = (TextView) view.findViewById(R.id.main_title);
        TextView subTitleView = (TextView) view.findViewById(R.id.sub_title);
        LinearLayout listItemsView = (LinearLayout) view.findViewById(R.id.list_items);

        ListTemplate1Bean.DirectiveBean.PayloadBean payloadBean = content.getDirective().getPayload();
        ListTemplate1Bean.DirectiveBean.PayloadBean.TitleBean titleBean = payloadBean.getTitle();
        mainTitleView.setText(titleBean.getMainTitle());
        subTitleView.setText(titleBean.getSubTitle());
        List<ListTemplate1Bean.DirectiveBean.PayloadBean.ListItemsBean> dataItems = payloadBean.getListItems();
        for (ListTemplate1Bean.DirectiveBean.PayloadBean.ListItemsBean item : dataItems) {
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setGravity(Gravity.CENTER_VERTICAL);
            String leftTextField = item.getLeftTextField();
            String rightTextField = item.getRightTextField();
            TextView leftTv = new TextView(context);
            TextView rightTv = new TextView(context);
            leftTv.setText(leftTextField);
            leftTv.setTextSize(20);
            leftTv.setTextColor(Color.parseColor("#7B7D7B"));
            rightTv.setText(rightTextField);
            ll.addView(leftTv);
            rightTv.setPadding(25, 0, 0, 16);
            rightTv.setTextSize(24);
            ll.addView(rightTv);
            listItemsView.addView(ll);
        }
        return view;
    }

    @NonNull
    private View setWeatherView(Context context, WeatherTemplateBean content) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.fragment_weather_template, null);

        TextView mainTitleView = (TextView) view.findViewById(R.id.main_title);
        TextView subTitleView = (TextView) view.findViewById(R.id.sub_title);
        ImageView currentWeatherImgView = (ImageView) view.findViewById(R.id.today_img);
        TextView currentWeatherTv = (TextView) view.findViewById(R.id.current_weather);
        TextView currentHighTempTv = (TextView) view.findViewById(R.id.current_high_temp);
        TextView currentLowTempTv = (TextView) view.findViewById(R.id.current_low_temp);
        LinearLayout weatherContainer = (LinearLayout) view.findViewById(R.id.week_day_container);

        WeatherTemplateBean.DirectiveBean.PayloadBean payloadBean = content.getDirective().getPayload();
        WeatherTemplateBean.DirectiveBean.PayloadBean.TitleBean titleBean = payloadBean.getTitle();
        mainTitleView.setText(titleBean.getMainTitle());
        subTitleView.setText(titleBean.getSubTitle());

        List<WeatherTemplateBean.DirectiveBean.PayloadBean.CurrentWeatherIconBean.SourcesBeanX> currentWeatherSources = payloadBean.getCurrentWeatherIcon().getSources();
        String currentWeatherIconUrl = currentWeatherSources.get(currentWeatherSources.size() - 1).getUrl();
        Glide.with(context).load(currentWeatherIconUrl).into(currentWeatherImgView);
        currentWeatherTv.setText(payloadBean.getCurrentWeather());
        currentHighTempTv.setText(payloadBean.getHighTemperature().getValue());
        currentLowTempTv.setText(payloadBean.getLowTemperature().getValue());


        List<WeatherTemplateBean.DirectiveBean.PayloadBean.WeatherForecastBean> weatherForecast = payloadBean.getWeatherForecast();

        for (int i = 0; i < 3; i++) {
            View oneDayTempView = View.inflate(context, R.layout.one_day_weather_template, null);
            WeatherTemplateBean.DirectiveBean.PayloadBean.WeatherForecastBean weatherForecastBean = weatherForecast.get(i);
            String url = weatherForecastBean.getImage().getSources().get(2).getUrl();
            ImageView weatherIcon = (ImageView) oneDayTempView.findViewById(R.id.weather_icon);
            Glide.with(context).load(url).into(weatherIcon);
            TextView weekDayView = (TextView) oneDayTempView.findViewById(R.id.week_day);
            weekDayView.setText(weatherForecastBean.getDay());
            TextView hTempTv = (TextView) oneDayTempView.findViewById(R.id.high_temp);
            hTempTv.setText(weatherForecastBean.getHighTemperature());
            TextView lTempTv = (TextView) oneDayTempView.findViewById(R.id.low_temp);
            lTempTv.setText(weatherForecastBean.getLowTemperature());
            weatherContainer.addView(oneDayTempView);
        }
        return view;
    }

    @NonNull
    private View setTem2View(Context context, Template2Bean content) {
        View view;
        Log.d(TAG, "setView: Template2Bean");
        view = LayoutInflater.from(context).inflate(R.layout.fragment_template2, null);
        TextView mainTitleView = (TextView) view.findViewById(R.id.main_title);
        TextView subTitleView = (TextView) view.findViewById(R.id.sub_title);
        ImageView mainImage = (ImageView) view.findViewById(R.id.main_image);
        TextView textContentView = (TextView) view.findViewById(R.id.text_content);

        Template2Bean.DirectiveBean.PayloadBean payloadBean = content.getDirective().getPayload();
        Template2Bean.DirectiveBean.PayloadBean.TitleBean titleBean = payloadBean.getTitle();
        mainTitleView.setText(titleBean.getMainTitle());
        subTitleView.setText(titleBean.getSubTitle());
        String url = payloadBean.getImage().getSources().get(0).getUrl();
        Glide.with(context).load(url).into(mainImage);
        textContentView.setText(payloadBean.getTextField());
        return view;
    }

    @NonNull
    private View setTem1View(Context context, Template1Bean content) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.fragment_template1, null);
        Log.d(TAG, "setView: Template1Bean");
        TextView mainTitleView = (TextView) view.findViewById(R.id.main_title);
        TextView subTitleView = (TextView) view.findViewById(R.id.sub_title);
        TextView textContentView = (TextView) view.findViewById(R.id.text_content);

        Template1Bean.DirectiveBean.PayloadBean payloadBean = content.getDirective().getPayload();
        Template1Bean.DirectiveBean.PayloadBean.TitleBean titleBean = payloadBean.getTitle();
        mainTitleView.setText(titleBean.getMainTitle());
        subTitleView.setText(titleBean.getSubTitle());
        textContentView.setText(payloadBean.getTextField());
        return view;
    }


}
