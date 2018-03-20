package com.willblaschko.android.alexavoicelibrary.display;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.willblaschko.android.alexa.beans.ListTemplate1Bean;
import com.willblaschko.android.alexavoicelibrary.R;

import java.util.List;


public class ListTemplate1Fragment extends Fragment {

    public static ListTemplate1Fragment newInstance() {
        return new ListTemplate1Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        ListTemplate1Bean renderObj = args.getParcelable("args");

        View view = inflater.inflate(R.layout.fragment_listtemplate1, container, false);

        TextView mainTitleView = (TextView) view.findViewById(R.id.main_title);
        TextView subTitleView = (TextView) view.findViewById(R.id.sub_title);
        LinearLayout listItemsView = (LinearLayout) view.findViewById(R.id.list_items);

        ListTemplate1Bean.DirectiveBean.PayloadBean payloadBean = renderObj.getDirective().getPayload();
        ListTemplate1Bean.DirectiveBean.PayloadBean.TitleBean titleBean = payloadBean.getTitle();
        mainTitleView.setText(titleBean.getMainTitle());
        subTitleView.setText(titleBean.getSubTitle());
        List<ListTemplate1Bean.DirectiveBean.PayloadBean.ListItemsBean> dataItems = payloadBean.getListItems();
        for (ListTemplate1Bean.DirectiveBean.PayloadBean.ListItemsBean item : dataItems) {
            LinearLayout ll = new LinearLayout(getActivity());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setGravity(Gravity.CENTER_VERTICAL);
            String leftTextField = item.getLeftTextField();
            String rightTextField = item.getRightTextField();
            TextView leftTv = new TextView(getActivity());
            TextView rightTv = new TextView(getActivity());
            leftTv.setText(leftTextField);
            rightTv.setText(rightTextField);
            ll.addView(leftTv);
            rightTv.setPadding(20, 0, 0, 0);
            ll.addView(rightTv);
            listItemsView.addView(ll);
        }

        return view;
    }
}
