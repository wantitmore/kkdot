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
import com.willblaschko.android.alexa.beans.Template2Bean;
import com.willblaschko.android.alexavoicelibrary.R;


public class Template2Fragment extends Fragment {

    public static Template2Fragment newInstance() {
        return new Template2Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        Template2Bean renderObj = args.getParcelable("args");

        View view = inflater.inflate(R.layout.fragment_template2, container, false);

        TextView mainTitleView = (TextView) view.findViewById(R.id.main_title);
        TextView subTitleView = (TextView) view.findViewById(R.id.sub_title);
        ImageView mainImage = (ImageView) view.findViewById(R.id.main_image);
        TextView textContentView = (TextView) view.findViewById(R.id.text_content);

        Template2Bean.DirectiveBean.PayloadBean payloadBean = renderObj.getDirective().getPayload();
        Template2Bean.DirectiveBean.PayloadBean.TitleBean titleBean = payloadBean.getTitle();
        mainTitleView.setText(titleBean.getMainTitle());
        subTitleView.setText(titleBean.getSubTitle());
        String url = payloadBean.getImage().getSources().get(0).getUrl();
        Glide.with(this).load(url).into(mainImage);
        textContentView.setText(payloadBean.getTextField());

        return view;
    }
}
