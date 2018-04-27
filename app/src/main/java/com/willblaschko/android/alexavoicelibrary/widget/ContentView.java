package com.willblaschko.android.alexavoicelibrary.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.willblaschko.android.alexa.beans.Template1Bean;
import com.willblaschko.android.alexa.beans.Template2Bean;
import com.willblaschko.android.alexavoicelibrary.R;

/**
 * Created by user001 on 2018-4-26.
 */

public class ContentView {

    private static final String TAG = "Tem1View";



    public View setView(Context context, Object content) {
        View view = null;
        if (content instanceof Template1Bean) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_template1, null);
            Log.d(TAG, "setView: Template1Bean");
            TextView mainTitleView = (TextView) view.findViewById(R.id.main_title);
            TextView subTitleView = (TextView) view.findViewById(R.id.sub_title);
            TextView textContentView = (TextView) view.findViewById(R.id.text_content);

            Template1Bean.DirectiveBean.PayloadBean payloadBean = ((Template1Bean) content).getDirective().getPayload();
            Template1Bean.DirectiveBean.PayloadBean.TitleBean titleBean = payloadBean.getTitle();
            mainTitleView.setText(titleBean.getMainTitle());
            subTitleView.setText(titleBean.getSubTitle());
            textContentView.setText(payloadBean.getTextField());
        } else if (content instanceof Template2Bean) {
            Log.d(TAG, "setView: Template2Bean");
            view = LayoutInflater.from(context).inflate(R.layout.fragment_template2, null);
            TextView mainTitleView = (TextView) view.findViewById(R.id.main_title);
            TextView subTitleView = (TextView) view.findViewById(R.id.sub_title);
            ImageView mainImage = (ImageView) view.findViewById(R.id.main_image);
            TextView textContentView = (TextView) view.findViewById(R.id.text_content);

            Template2Bean.DirectiveBean.PayloadBean payloadBean = ((Template2Bean) content).getDirective().getPayload();
            Template2Bean.DirectiveBean.PayloadBean.TitleBean titleBean = payloadBean.getTitle();
            mainTitleView.setText(titleBean.getMainTitle());
            subTitleView.setText(titleBean.getSubTitle());
            String url = payloadBean.getImage().getSources().get(0).getUrl();
            Glide.with(context).load(url).into(mainImage);
            textContentView.setText(payloadBean.getTextField());
        }
        return view;
    }




}
