package com.willblaschko.android.alexavoicelibrary.actions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.willblaschko.android.alexa.beans.Template1Bean;
import com.willblaschko.android.alexa.beans.Template2Bean;
import com.willblaschko.android.alexa.beans.WeatherTemplateBean;
import com.willblaschko.android.alexavoicelibrary.R;
import com.willblaschko.android.alexavoicelibrary.display.DisplayCardActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author will on 5/30/2016.
 */

public class SendTextActionFragment extends BaseListenerFragment {

    EditText search;
    View button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_action_type, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);

        search = (EditText) view.findViewById(R.id.search);
        button = view.findViewById(R.id.button);

        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter_left" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    search();
                    return true;
                }
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void search(){
        String text = search.getText().toString();
        alexaManager.sendTextRequest(text, getRequestCallback());
    }

    @Override
    public void startListening() {
        search.setText("");
        search.requestFocus();
    }

//    @Override
//    protected String getTitle() {
//        return getString(R.string.fragment_action_send_text);
//    }

    @Override
    protected int getRawCode() {
        return R.raw.code_text;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(Object renderObj) {
        Intent intent = new Intent(getActivity(), DisplayCardActivity.class);
        if (renderObj instanceof Template1Bean) {
            Log.d(SendTextActionFragment.class.getSimpleName(), "app Template1Bean = " + renderObj.toString());
            intent.putExtra("args", (Template1Bean)renderObj);
        } else if (renderObj instanceof Template2Bean) {
            Log.d(SendTextActionFragment.class.getSimpleName(), "app Template2Bean = " + renderObj.toString());
            intent.putExtra("args", (Template2Bean)renderObj);
        } else if (renderObj instanceof WeatherTemplateBean) {
            Log.d(SendTextActionFragment.class.getSimpleName(), "app weather = " + renderObj.toString());
            intent.putExtra("args", (WeatherTemplateBean)renderObj);
        }
        startActivity(intent);
    }
}
