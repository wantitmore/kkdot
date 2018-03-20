package com.willblaschko.android.alexavoicelibrary.display;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.willblaschko.android.alexa.beans.Template1Bean;
import com.willblaschko.android.alexavoicelibrary.R;


public class Template1Fragment extends Fragment {

    public static Template1Fragment newInstance() {
        return new Template1Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        Template1Bean renderObj = args.getParcelable("args");

        View view = inflater.inflate(R.layout.fragment_template1, container, false);

        TextView mainTitleView = (TextView) view.findViewById(R.id.main_title);
        TextView subTitleView = (TextView) view.findViewById(R.id.sub_title);
        TextView textContentView = (TextView) view.findViewById(R.id.text_content);

        Template1Bean.DirectiveBean.PayloadBean payloadBean = renderObj.getDirective().getPayload();
        Template1Bean.DirectiveBean.PayloadBean.TitleBean titleBean = payloadBean.getTitle();
        mainTitleView.setText(titleBean.getMainTitle());
        subTitleView.setText(titleBean.getSubTitle());
        textContentView.setText(payloadBean.getTextField());

        return view;
    }
}
