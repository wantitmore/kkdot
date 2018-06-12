package com.konka.alexa.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.konka.alexa.alexalib.beans.ListTemplate1Bean;
import com.konka.alexa.alexalib.beans.Template1Bean;
import com.konka.alexa.alexalib.beans.Template2Bean;
import com.konka.alexa.alexalib.beans.WeatherTemplateBean;
import com.konka.alexa.R;

import java.util.List;

import app.com.tvrecyclerview.TvRecyclerView;

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
        TvRecyclerView listItemsView = (TvRecyclerView) view.findViewById(R.id.list_items);
        ImageView skillsIcon = (ImageView) view.findViewById(R.id.iv_skills_icon);

        ListTemplate1Bean.DirectiveBean.PayloadBean payloadBean = content.getDirective().getPayload();
        ListTemplate1Bean.DirectiveBean.PayloadBean.TitleBean titleBean = payloadBean.getTitle();
        try {
            Glide.with(context).load(payloadBean.getSkillIcon().getSources().get(0).getUrl()).into(skillsIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainTitleView.setText(titleBean.getMainTitle());
        subTitleView.setText(titleBean.getSubTitle());
        List<ListTemplate1Bean.DirectiveBean.PayloadBean.ListItemsBean> mDataItems = payloadBean.getListItems();
        /*for (ListTemplate1Bean.DirectiveBean.PayloadBean.ListItemsBean item : mDataItems) {
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
        }*/
        ListAdapter adapter = new ListAdapter(context, mDataItems);
        listItemsView.setLayoutManager(new LinearLayoutManager(context));
        listItemsView.setAdapter(adapter);
        listItemsView.setItemSelected(5);
        listItemsView.setScrollMode(1);
        return view;
    }

    class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyHolder> {
        private Context context;
        private List<ListTemplate1Bean.DirectiveBean.PayloadBean.ListItemsBean> beans;

        public ListAdapter(Context context, List<ListTemplate1Bean.DirectiveBean.PayloadBean.ListItemsBean>beans) {
            this.context = context;
            this.beans = beans;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            ListTemplate1Bean.DirectiveBean.PayloadBean.ListItemsBean bean = beans.get(position);
            holder.num.setText(bean.getLeftTextField());
            holder.item_content.setText(bean.getRightTextField());
            holder.itemView.setFocusable(true);
        }

        @Override
        public int getItemCount() {
            return beans == null ? 0 : beans.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {

            private final TextView num;
            private final TextView item_content;

            private MyHolder(View itemView) {
                super(itemView);
                num = (TextView) itemView.findViewById(R.id.num);
                item_content = (TextView) itemView.findViewById(R.id.item_content);
                itemView.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        Log.d(TAG, "onKey: keycode is " + keyCode);
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            context.sendBroadcast(new Intent("com.konka.alexa.back"));
                        }
                        return false;
                    }
                });
            }
        }
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
        ImageView skillsIcon = (ImageView) view.findViewById(R.id.iv_skills_icon);

        textContentView.setMovementMethod(new ScrollingMovementMethod());

        Template2Bean.DirectiveBean.PayloadBean payloadBean = content.getDirective().getPayload();
        Template2Bean.DirectiveBean.PayloadBean.TitleBean titleBean = payloadBean.getTitle();
        mainTitleView.setText(titleBean.getMainTitle());
        subTitleView.setText(titleBean.getSubTitle());
        String url = payloadBean.getImage().getSources().get(0).getUrl();
        Glide.with(context).load(url).into(mainImage);
        textContentView.setText(payloadBean.getTextField());
        try {
            Glide.with(context).load(payloadBean.getSkillIcon().getSources().get(0).getUrl()).into(skillsIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        ImageView skillsIcon = (ImageView) view.findViewById(R.id.iv_skills_icon);
        textContentView.setMovementMethod(new ScrollingMovementMethod());
        Template1Bean.DirectiveBean.PayloadBean payloadBean = content.getDirective().getPayload();
        Template1Bean.DirectiveBean.PayloadBean.TitleBean titleBean = payloadBean.getTitle();
        mainTitleView.setText(titleBean.getMainTitle());
        subTitleView.setText(titleBean.getSubTitle());
        textContentView.setText(payloadBean.getTextField());
        try {
            Glide.with(context).load(payloadBean.getSkillIcon().getSources().get(0).getUrl()).into(skillsIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


}
